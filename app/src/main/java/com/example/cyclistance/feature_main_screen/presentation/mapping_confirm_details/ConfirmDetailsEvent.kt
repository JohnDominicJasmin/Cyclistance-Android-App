package com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details

import androidx.compose.ui.text.input.TextFieldValue

sealed class ConfirmDetailsEvent{
    object Save: ConfirmDetailsEvent()
    data class SelectBikeType(val bikeType: String): ConfirmDetailsEvent()
    data class SelectDescription(val description: String): ConfirmDetailsEvent()
    data class EnteredMessage(val message: TextFieldValue): ConfirmDetailsEvent()


}
