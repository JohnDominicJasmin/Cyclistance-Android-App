package com.example.cyclistance.feature_mapping_screen.presentation.mapping_confirm_details

sealed class ConfirmDetailsUiEvent{
    data class ShowToastMessage(val message: String): ConfirmDetailsUiEvent()
    object ShowMappingScreen: ConfirmDetailsUiEvent()
}
