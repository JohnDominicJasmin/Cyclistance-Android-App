package com.example.cyclistance.feature_authentication.presentation.authentication_email

import android.os.CountDownTimer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.AuthConstants.EMAIL_AUTH_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.AuthConstants.ONE_SECOND_TO_MILLIS
import com.example.cyclistance.core.utils.constants.AuthConstants.REFRESH_EMAIL_INTERVAL
import com.example.cyclistance.core.utils.constants.AuthConstants.TIMER_COUNTS
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
@HiltViewModel
class EmailAuthViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
    private val defaultDispatcher: CoroutineDispatcher
    ) : ViewModel() {

    private lateinit var verificationTimer: CountDownTimer
    private var job: Job? = null


    private val _state: MutableStateFlow<EmailAuthState> = MutableStateFlow(savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] ?: EmailAuthState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<EmailAuthUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<EmailAuthUiEvent> = _eventFlow.asSharedFlow()


    init{
        _state.update { it.copy(savedAccountEmail = authUseCase.getEmailUseCase() ?: "") }
        savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] = state.value

    }

    fun onEvent(event: EmailAuthEvent) {
        when (event) {
            is EmailAuthEvent.RefreshEmail -> {
                viewModelScope.launch(context = defaultDispatcher) {
                    reloadEmail()
                }
            }

            is EmailAuthEvent.ResendEmailVerification -> {
                _state.update { it.copy(isEmailResend = true) }
            }

            is EmailAuthEvent.SendEmailVerification -> {
                sendEmailVerification()
            }
            is EmailAuthEvent.StartTimer -> {
                viewModelScope.launch(context = defaultDispatcher) {
                    startTimer()
                }
            }
            is EmailAuthEvent.SubscribeEmailVerification -> {
                job = viewModelScope.launch(context = defaultDispatcher) {
                    refreshEmailAsync()
                }
            }
        }
        savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] = state.value
    }


    private suspend fun verifyEmail() {
        runCatching {
            authUseCase.isEmailVerifiedUseCase() == true
        }.onSuccess { emailIsVerified ->
            _state.update { it.copy(isLoading = false) }
            showVerifyEmailResult(emailIsVerified)
        }.onFailure {
            _state.update { it.copy(isLoading = false) }
        }
        savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] = state.value
    }

    private suspend fun showVerifyEmailResult(emailIsVerified: Boolean) {
        if (emailIsVerified) {
            _eventFlow.emit(EmailAuthUiEvent.EmailVerificationSuccess)
            delay(300)
            job?.cancel()
        } else {
            _eventFlow.emit(EmailAuthUiEvent.EmailVerificationFailed)
        }
    }

    private suspend fun refreshEmailAsync() {
        coroutineScope {
            while (this.isActive) {
                delay(REFRESH_EMAIL_INTERVAL)
                reloadEmail()
            }
        }
    }

    private suspend fun reloadEmail() {

        coroutineScope {
            runCatching {
                authUseCase.reloadEmailUseCase()
            }.onSuccess { isEmailReloaded ->
                _state.update { it.copy(isLoading = false) }
                savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] = state.value
                if (isEmailReloaded) {
                    verifyEmail()
                } else {
                    _eventFlow.emit(EmailAuthUiEvent.ReloadEmailFailed())
                }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                when (exception) {
                    is AuthExceptions.NetworkException -> {
                        _eventFlow.emit(value = EmailAuthUiEvent.NoInternetConnection)
                    }
                    else -> {
                        Timber.e("${this.javaClass.name}: ${exception.message}")
                    }
                }
                savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] = state.value
            }
        }
    }


    // TODO: move to repository
    private fun startTimer() {

        viewModelScope.launch(Dispatchers.Main) {

            _eventFlow.emit(value = EmailAuthUiEvent.TimerStarted)
            savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] = state.value
            verificationTimer = object : CountDownTimer(TIMER_COUNTS, ONE_SECOND_TO_MILLIS) {
                override fun onTick(millisUntilFinished: Long) {
                    val timeLeft = millisUntilFinished / ONE_SECOND_TO_MILLIS
                    _state.update { it.copy(secondsLeft = timeLeft.toInt()) }
                    savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] = state.value
                    Timber.d("TimeLeft is $timeLeft")
                }

                override fun onFinish() {
                    stopTimer()
                    this@launch.launch {
                        _eventFlow.emit(value = EmailAuthUiEvent.TimerStopped)
                    }
                    savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] = state.value
                }
            }.start()

        }
    }

    // TODO: move to repository
    private fun stopTimer() {
        if (::verificationTimer.isInitialized) {
            verificationTimer.cancel()
        }
    }

    // TODO: Move texts to Constants
    private fun sendEmailVerification() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                _state.update { it.copy(isLoading = true) }
                authUseCase.sendEmailVerificationUseCase()
            }.onSuccess { isEmailVerificationSent ->
                _state.update { it.copy(isLoading = false) }
                if (!state.value.isEmailResend) {
                    return@launch
                }

                if (!isEmailVerificationSent) {
                    _eventFlow.emit(EmailAuthUiEvent.EmailVerificationNotSent())
                    return@launch
                }

                _eventFlow.emit(value = EmailAuthUiEvent.EmailVerificationSent)
            }.onFailure {

                _state.update { it.copy(isLoading = false) }
                if (state.value.isEmailResend) {
                    _eventFlow.emit(value = EmailAuthUiEvent.SendEmailVerificationFailed)
                }

            }
        }.invokeOnCompletion {
            savedStateHandle[EMAIL_AUTH_VM_STATE_KEY] = state.value
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        job?.cancel()
    }
}