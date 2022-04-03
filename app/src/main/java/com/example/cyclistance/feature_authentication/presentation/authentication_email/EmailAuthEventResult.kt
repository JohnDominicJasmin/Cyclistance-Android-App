package com.example.cyclistance.feature_authentication.presentation.authentication_email

import androidx.annotation.RawRes

sealed class EmailAuthEventResult{
    object ShowMappingScreen: EmailAuthEventResult()
    object UserEmailIsNotVerified: EmailAuthEventResult()
    object ShowNoInternetScreen: EmailAuthEventResult()
    data class ShowAlertDialog(
        val title: String = "",
        val description: String = "",
        @RawRes val imageResId: Int = -1
    ):EmailAuthEventResult()
    data class ShowToastMessage(val message: String): EmailAuthEventResult()
}
