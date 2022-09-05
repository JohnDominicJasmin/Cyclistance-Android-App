package com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details

import androidx.compose.ui.text.input.TextFieldValue

data class ConfirmDetailsState(
    val bikeType: String = "",
    val bikeTypeErrorMessage: String = "",
    val description: String = "",
    val descriptionErrorMessage: String = "",
    val address: String = "",
    val message: TextFieldValue = TextFieldValue(),
    val isLoading: Boolean = false,
)
