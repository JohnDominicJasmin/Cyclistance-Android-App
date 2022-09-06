package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import androidx.annotation.RawRes

sealed class SignInUiEvent {
    object RefreshEmail: SignInUiEvent()
    object ShowNoInternetScreen : SignInUiEvent()
    data class ShowAlertDialog(
        val title: String = "",
        val description: String = "",
        @RawRes val imageResId: Int = -1) : SignInUiEvent()
    object ShowMappingScreen: SignInUiEvent()
    data class ShowToastMessage(val message: String) : SignInUiEvent()


}