package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import androidx.annotation.RawRes

sealed class SignInEventResult {
    object RefreshEmail: SignInEventResult()
    object ShowInternetScreen : SignInEventResult()
    object ShowProgressBar: SignInEventResult()
    object HideProgressBar: SignInEventResult()
    data class ShowAlertDialog(val title: String, val description: String, @RawRes val imageResId: Int) : SignInEventResult()
    data class ShowPasswordTextFieldError(val errorMessage: String): SignInEventResult()
    data class ShowEmailTextFieldError(val errorMessage: String): SignInEventResult()

}