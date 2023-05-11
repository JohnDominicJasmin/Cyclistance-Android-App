package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.event

import android.app.Activity
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential

sealed class SignInVmEvent {
    data class SignInVmFacebook(val activity: Activity?) : SignInVmEvent()
    data class SignInVmGoogle(var authCredential: SignInCredential.Google) : SignInVmEvent()
    data class SignInVmWithEmailAndPassword(val email: String, val password: String) : SignInVmEvent()
}