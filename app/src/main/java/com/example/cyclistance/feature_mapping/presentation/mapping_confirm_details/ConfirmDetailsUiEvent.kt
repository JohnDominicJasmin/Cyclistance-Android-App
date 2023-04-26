package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details

sealed class ConfirmDetailsUiEvent{
    object ConfirmDetailsSuccess: ConfirmDetailsUiEvent()
    data class UserError(val reason:String = "User not found"): ConfirmDetailsUiEvent()
    data class UnexpectedError(val reason:String = "An unexpected error occurred."): ConfirmDetailsUiEvent()
}
