package com.example.cyclistance.feature_authentication.presentation.authentication_email

import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.cyclistance.core.utils.constants.AuthConstants.ONE_SECOND_TO_MILLIS
import com.example.cyclistance.core.utils.constants.AuthConstants.REFRESH_EMAIL_INTERVAL
import com.example.cyclistance.core.utils.constants.AuthConstants.TIMER_COUNTS
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import io.github.farhanroy.composeawesomedialog.R as R1
@HiltViewModel
class EmailAuthViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private lateinit var verificationTimer: CountDownTimer
    private var job: Job? = null


    private val _state: MutableStateFlow<EmailAuthState> = MutableStateFlow(EmailAuthState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<EmailAuthUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<EmailAuthUiEvent> = _eventFlow.asSharedFlow()


    init{
        _state.update { it.copy(savedAccountEmail = authUseCase.getEmailUseCase() ?: "") }
    }

    fun onEvent(event: EmailAuthEvent) {
        when (event) {
            is EmailAuthEvent.RefreshEmail -> {
                viewModelScope.launch {
                    reloadEmail()
                }
            }

            is EmailAuthEvent.DismissNoInternetScreen -> {
                _state.update { it.copy(hasInternet = true) }
            }

            is EmailAuthEvent.SendEmailVerification -> {
                sendEmailVerification()
            }
            is EmailAuthEvent.StartTimer -> {
                viewModelScope.launch {
                    startTimer()
                }
            }
            is EmailAuthEvent.SubscribeEmailVerification -> {
                job = viewModelScope.launch {
                    refreshEmailAsync()
                }
            }

            is EmailAuthEvent.ResendButtonClick -> {
                _state.update { it.copy(isEmailResendClicked = true) }
            }

            is EmailAuthEvent.DismissAlertDialog -> {
                _state.update { it.copy(alertDialogModel = AlertDialogModel()) }
            }
        }
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
    }

    private suspend fun showVerifyEmailResult(emailIsVerified: Boolean) {
        if (emailIsVerified) {
            _eventFlow.emit(EmailAuthUiEvent.ShowMappingScreen)
            delay(300)
            job?.cancel()
        } else {
            _eventFlow.emit(EmailAuthUiEvent.ShowEmailAuthScreen)
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
                if (isEmailReloaded) {
                    verifyEmail()
                } else {
                    _eventFlow.emit(EmailAuthUiEvent.ShowToastMessage(message = "Sorry, something went wrong. Please try again."))
                }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                when (exception) {
                    is AuthExceptions.NetworkException -> {
                        _state.update { it.copy(hasInternet = false) }
                    }
                    else -> {
                        Timber.e("${this.javaClass.name}: ${exception.message}")
                    }
                }
            }
        }
    }


    private fun startTimer() {
        _state.update { it.copy(isTimerRunning = true) }
        verificationTimer = object : CountDownTimer(TIMER_COUNTS, ONE_SECOND_TO_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                val timeLeft = millisUntilFinished / ONE_SECOND_TO_MILLIS
                _state.update { it.copy(secondsLeft = timeLeft.toInt()) }
                Timber.d("TimeLeft is $timeLeft")
            }

            override fun onFinish() {
                stopTimer()
                _state.update { it.copy(isTimerRunning = false) }
            }
        }.start()

    }

    private fun stopTimer() {
        if (::verificationTimer.isInitialized) {
            verificationTimer.cancel()
        }
    }

    private fun sendEmailVerification() {
        viewModelScope.launch {
            runCatching {
                _state.update { it.copy(isLoading = true) }
                authUseCase.sendEmailVerificationUseCase()
            }.onSuccess { isEmailVerificationSent ->
                _state.update { it.copy(isLoading = false) }
                if (!state.value.isEmailResendClicked) {
                    return@launch
                }

                if (isEmailVerificationSent) {
                    _state.update {
                        it.copy(
                            alertDialogModel = AlertDialogModel(
                                title = "New Email Sent.",
                                description = "New verification email has been sent to your email address.",
                                icon = R1.raw.success))
                    }
                    return@launch
                }

                _eventFlow.emit(EmailAuthUiEvent.ShowToastMessage(message = "Sorry, something went wrong. Please try again."))

            }.onFailure {

                _state.update { it.copy(isLoading = false) }
                if (state.value.isEmailResendClicked) {
                    _state.update {
                        it.copy(
                            alertDialogModel = AlertDialogModel(
                                title = "Error",
                                description = "Sorry, something went wrong. Please try again.",
                                icon = R1.raw.error
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        job?.cancel()
    }
}