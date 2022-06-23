package com.example.cyclistance.feature_authentication.presentation.authentication_email

import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cyclistance.common.AuthConstants.ONE_SECOND_TO_MILLIS
import com.example.cyclistance.common.AuthConstants.REFRESH_EMAIL_INTERVAL
import com.example.cyclistance.common.AuthConstants.TIMER_COUNTS
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EmailAuthViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private lateinit var verificationTimer: CountDownTimer
    private var job: Job? = null


    private val _state: MutableState<EmailAuthState> = mutableStateOf(EmailAuthState())
    val state: State<EmailAuthState> = _state

    private val _eventFlow: MutableSharedFlow<EmailAuthUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<EmailAuthUiEvent> = _eventFlow.asSharedFlow()


    fun onEvent(event: EmailAuthEvent) {
        when(event){
            is EmailAuthEvent.RefreshEmail -> {
                viewModelScope.launch {
                    reloadEmail()
                }
            }
            is EmailAuthEvent.SendEmailVerification -> {
                viewModelScope.launch {
                    sendEmailVerification()
                }
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
                _state.value = state.value.copy(isEmailResendClicked =  true)
            }
        }
    }





    private suspend fun verifyEmail() {
        runCatching {
            authUseCase.isEmailVerifiedUseCase() == true
        }.onSuccess { emailIsVerified ->
            _state.value = state.value.copy(isLoading = false)

            if(emailIsVerified){
                _eventFlow.emit(EmailAuthUiEvent.ShowMappingScreen)
                delay(300)
                job?.let{
                    if(it.isActive){
                        it.cancel()
                    }
                }
            }else{
                _eventFlow.emit(EmailAuthUiEvent.ShowEmailAuthScreen)
            }
        }.onFailure {
            _state.value = state.value.copy(isLoading = false)
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

    private suspend fun reloadEmail(){
        runCatching {
            authUseCase.reloadEmailUseCase()
        }.onSuccess { isEmailReloaded ->
            _state.value = state.value.copy(isLoading = false)
            if(isEmailReloaded) {
                verifyEmail()
            }else{
                _eventFlow.emit(EmailAuthUiEvent.ShowToastMessage(message = "Sorry, something went wrong. Please try again."))
            }
        }.onFailure { exception ->
            _state.value = state.value.copy(isLoading = false)
            when (exception) {
                is AuthExceptions.InternetException -> {
                    _eventFlow.emit(EmailAuthUiEvent.ShowNoInternetScreen)
                }
                else -> {
                    Timber.e("${this.javaClass.name}: ${exception.message}")
                }
            }


        }
    }


    private fun startTimer() {
            _state.value = state.value.copy(isTimerRunning = true)
            verificationTimer = object : CountDownTimer(TIMER_COUNTS, ONE_SECOND_TO_MILLIS) {
                override fun onTick(millisUntilFinished: Long) {
                    val timeLeft = millisUntilFinished / ONE_SECOND_TO_MILLIS
                    _state.value = state.value.copy(secondsLeft = timeLeft.toInt())
                    Timber.d("TimeLeft is $timeLeft")
                }

                override fun onFinish() {
                    stopTimer()
                    _state.value = state.value.copy(isTimerRunning = false)

                }
            }.start()

        }

    private fun stopTimer() {
        if (::verificationTimer.isInitialized) {
            verificationTimer.cancel()
        }
    }

     private suspend fun sendEmailVerification() {
            runCatching {
                _state.value = state.value.copy(isLoading = true)
                authUseCase.sendEmailVerificationUseCase()
            }.onSuccess { isEmailVerificationSent ->
                _state.value = state.value.copy(isLoading = false)
                if (state.value.isEmailResendClicked) {
                    if (isEmailVerificationSent) {
                        _eventFlow.emit(
                            EmailAuthUiEvent.ShowAlertDialog(
                                title = "New Email Sent.",
                                description = "New verification email has been sent to your email address.",
                                imageResId = io.github.farhanroy.composeawesomedialog.R.raw.success
                            ))
                    } else {
                        _eventFlow.emit(EmailAuthUiEvent.ShowToastMessage(message = "Sorry, something went wrong. Please try again."))
                    }
                }



            }.onFailure {
                _state.value = state.value.copy(isLoading = false)
                if(state.value.isEmailResendClicked) {
                    _eventFlow.emit(
                        EmailAuthUiEvent.ShowAlertDialog(
                            title = "Error",
                            description = "There was an error trying to send the verification email. Please try again.",
                            imageResId = io.github.farhanroy.composeawesomedialog.R.raw.error
                        ))
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        job?.cancel()
    }
}