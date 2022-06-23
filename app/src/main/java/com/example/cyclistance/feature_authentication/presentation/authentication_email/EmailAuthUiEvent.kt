package com.example.cyclistance.feature_authentication.presentation.authentication_email

import androidx.annotation.RawRes

sealed class EmailAuthUiEvent{
    object ShowMappingScreen: EmailAuthUiEvent()
    object ShowEmailAuthScreen: EmailAuthUiEvent()
    object ShowNoInternetScreen: EmailAuthUiEvent()
    data class ShowAlertDialog(
        val title: String = "",
        val description: String = "",
        @RawRes val imageResId: Int = -1
    ):EmailAuthUiEvent()
    data class ShowToastMessage(val message: String): EmailAuthUiEvent()
}
