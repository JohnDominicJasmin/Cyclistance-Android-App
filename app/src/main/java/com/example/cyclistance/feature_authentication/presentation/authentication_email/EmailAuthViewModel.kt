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

    private val _reloadState: MutableState<AuthState<Boolean>> = mutableStateOf(AuthState())
    val reloadState: State<AuthState<Boolean>> = _reloadState

    private val _secondsLeftState: MutableState<Int> = mutableStateOf(0)
    val secondsLeftState: State<Int> = _secondsLeftState

    private val _isTimerFinished: MutableState<Boolean> = mutableStateOf(false)
    val isTimerFinished: State<Boolean> = _isTimerFinished // for button if not finished then show default button else show timer updating button


    //TODO WRAP THIS TO DATA CLASS
    private val _emailVerifyState: MutableState<AuthState<Boolean>> = mutableStateOf(AuthState())
    val emailVerifyState: State<AuthState<Boolean>> = _emailVerifyState

    private val _sendEmailVerificationState: MutableState<AuthState<Boolean>> = mutableStateOf(AuthState<Boolean>())
    val sendEmailVerificationState: State<AuthState<Boolean>> = _sendEmailVerificationState// if success then show dialog



    fun verifyEmail() {

        viewModelScope.launch {
            kotlin.runCatching {
                authUseCase.isEmailVerifiedUseCase() == true
            }.onSuccess { result ->
                _emailVerifyState.value = AuthState(result = result, isLoading = false)
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

    fun reloadEmail() {
        viewModelScope.launch {

            kotlin.runCatching {
                _reloadState.value = AuthState(isLoading = true)
                authUseCase.reloadEmailUseCase()
            }.onSuccess { result ->
                _reloadState.value = AuthState(isLoading = false, result = result)
            }.onFailure { exception ->
                when (exception) {
                    is AuthExceptions.InternetException -> {
                        _reloadState.value = AuthState(isLoading = false, result = false, error = exception.message ?: "No Internet Connection.")
                    }
                    else -> {
                        Timber.e("${this.javaClass.name}: ${exception.message}")
                    }
                }


            }

        }
    }


    fun startTimer() {
        viewModelScope.launch() {
            verificationTimer = object : CountDownTimer(TIMER_COUNTS, ONE_SECOND_TO_MILLIS) {
                override fun onTick(millisUntilFinished: Long) {
                    val timeLeft = millisUntilFinished / ONE_SECOND_TO_MILLIS
                    _secondsLeftState.value = timeLeft.toInt()
                }

                override fun onFinish() {
                    stopTimer()
                    _isTimerFinished.value = true
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
                _sendEmailVerificationState.value = AuthState(isLoading = false, result = false, error = it.message ?: "An unexpected error occurred.")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
        job?.cancel()
    }
}