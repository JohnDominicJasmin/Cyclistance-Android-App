package com.example.cyclistance.feature_authentication.presentation.authentication_email

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cyclistance.common.AuthConstants.ONE_SECOND_TO_MILLIS
import com.example.cyclistance.common.AuthConstants.REFRESH_EMAIL_INTERVAL
import com.example.cyclistance.common.AuthConstants.TIMER_COUNTS
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EmailAuthViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private lateinit var verificationTimer: CountDownTimer
    private var job: Job? = null




    private val _state = mutableStateOf(EmailAuthState())
    val state: State<EmailAuthState> = _state

    private val _sendEmailVerificationState = mutableStateOf(AuthState<Boolean>())
    val sendEmailVerificationState = _sendEmailVerificationState

    private val _verifyEmailState = mutableStateOf(AuthState<Boolean>())
    val verifyEmailState = _verifyEmailState

    private val _reloadEmailState = mutableStateOf(AuthState<Boolean>())
    val reloadEmailState = _reloadEmailState


    fun verifyEmail() {

        viewModelScope.launch {
            kotlin.runCatching {
                _verifyEmailState.value = AuthState(isLoading = true)
                authUseCase.isEmailVerifiedUseCase() == true
            }.onSuccess { result ->
                _verifyEmailState.value = AuthState(result = result, isLoading = false)
            }
        }
    }




     fun refreshEmailAsync() {
        job = viewModelScope.launch {
            while (isActive) {
                delay(REFRESH_EMAIL_INTERVAL)
                reloadEmail()
            }
        }
    }

    private suspend fun reloadEmail(){
        kotlin.runCatching {
            _reloadEmailState.value = AuthState(isLoading = true)
            authUseCase.reloadEmailUseCase()
        }.onSuccess { result ->
            _reloadEmailState.value = AuthState(isLoading = false, result = result)
        }.onFailure { exception ->
            when (exception) {
                is AuthExceptions.InternetException -> {
                    _reloadEmailState.value = AuthState( isLoading = false, result = false, internetExceptionMessage = exception.message ?: "No Internet Connection.")
                }
                else -> {
                    Timber.e("${this.javaClass.name}: ${exception.message}")
                }
            }


        }
    }
    fun refreshEmail() {
        viewModelScope.launch {
            reloadEmail()
        }
    }


    fun startTimer() {
        viewModelScope.launch() {
            _state.value = EmailAuthState(isTimerRunning = true)
            verificationTimer = object : CountDownTimer(TIMER_COUNTS, ONE_SECOND_TO_MILLIS) {
                override fun onTick(millisUntilFinished: Long) {
                    val timeLeft = millisUntilFinished / ONE_SECOND_TO_MILLIS
                    _state.value = EmailAuthState(secondsLeft = timeLeft.toInt())
                }

                override fun onFinish() {
                    stopTimer()
                    _state.value = EmailAuthState(isTimerRunning = false)

                }
            }.start()

        }
    }

    private fun stopTimer() {
        if (::verificationTimer.isInitialized) {
            verificationTimer.cancel()
        }
    }

     fun sendEmailVerification() {
        viewModelScope.launch {
            kotlin.runCatching {
                _sendEmailVerificationState.value = AuthState(isLoading = true)
                authUseCase.sendEmailVerificationUseCase()
            }.onSuccess { result ->
                _sendEmailVerificationState.value = AuthState(isLoading = false, result = result)
            }.onFailure {
                _sendEmailVerificationState.value =  AuthState(isLoading = false, result = false, sendEmailExceptionMessage = it.message ?: "An unexpected error occurred.")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        job?.cancel()
    }
}