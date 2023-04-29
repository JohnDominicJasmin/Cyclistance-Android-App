package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.app.Activity
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential

sealed class SignInEvent {
    data class SignInFacebook(val activity: Activity?) : SignInEvent()
    data class SignInGoogle(var authCredential: SignInCredential.Google) : SignInEvent()
    data class SignInWithEmailAndPassword(val email: String, val password: String) : SignInEvent()
}