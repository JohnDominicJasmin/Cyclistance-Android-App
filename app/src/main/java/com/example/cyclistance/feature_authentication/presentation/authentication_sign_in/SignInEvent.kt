package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.app.Activity
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential

sealed class SignInEvent{
    data class SignInFacebook(val activity: Activity?): SignInEvent()
    data class SignInGoogle(var authCredential: SignInCredential.Google): SignInEvent()
    object SignInDefault: SignInEvent()
    data class EnterEmail(val email: String) : SignInEvent()
    data class EnterPassword(val password: String) : SignInEvent()
    object TogglePasswordVisibility: SignInEvent()
    object DismissAlertDialog : SignInEvent()
    object DismissNoInternetDialog: SignInEvent()
}