package com.example.cyclistance.feature_authentication.presentation.auth_sign_in.event

import android.app.Activity
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential

sealed class SignInVmEvent {
    data class SignInFacebook(val activity: Activity?) : SignInVmEvent()
    data class SignInGoogle(var authCredential: SignInCredential.Google) : SignInVmEvent()
    data class SignInWithEmailAndPassword(val email: String, val password: String) : SignInVmEvent()
    object AgreedToPrivacyPolicy : SignInVmEvent()
}