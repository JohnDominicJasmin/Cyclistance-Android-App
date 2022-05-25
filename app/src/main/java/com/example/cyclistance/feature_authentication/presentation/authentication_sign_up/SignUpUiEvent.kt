package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.annotation.RawRes

sealed class SignUpUiEvent{
    object ShowNoInternetScreen: SignUpUiEvent()
    object ShowEmailAuthScreen: SignUpUiEvent()
    data class ShowToastMessage(val message: String) : SignUpUiEvent()
    data class ShowAlertDialog(
        val title: String = "",
        val description: String = "",
        @RawRes val imageResId: Int = -1
    ): SignUpUiEvent()

}
