package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.annotation.RawRes
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInEventResult

sealed class SignUpEventResult{
    object ShowInternetScreen: SignUpEventResult()
    data class ShowAlertDialog(
        val title: String = "",
        val description: String = "",
        @RawRes val imageResId: Int = -1
    ): SignUpEventResult()
    object ShowEmailAuthScreen: SignUpEventResult()
    data class ShowToastMessage(val message: String) : SignUpEventResult()
}
