package com.example.cyclistance.feature_authentication.presentation.authentication_email



sealed class EmailAuthUiEvent{
    object ShowMappingScreen: EmailAuthUiEvent()
    object ShowEmailAuthScreen: EmailAuthUiEvent()
    data class ShowToastMessage(val message: String): EmailAuthUiEvent()
}
