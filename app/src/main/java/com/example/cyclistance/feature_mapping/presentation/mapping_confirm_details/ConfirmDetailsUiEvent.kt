package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details

sealed class ConfirmDetailsUiEvent{
    object ConfirmDetailsSuccess: ConfirmDetailsUiEvent()
    data class UserError(val reason:String = "User not found"): ConfirmDetailsUiEvent()
    data class UnexpectedError(val reason:String = "An unexpected error occurred."): ConfirmDetailsUiEvent()
    data class GetSavedBikeType(val bikeType: String): ConfirmDetailsUiEvent()
    data class GetSavedAddress(val address: String): ConfirmDetailsUiEvent()
    object NoInternetConnection:ConfirmDetailsUiEvent()
    data class InvalidAddress(val reason:String): ConfirmDetailsUiEvent()
    data class InvalidBikeType(val reason:String): ConfirmDetailsUiEvent()
    data class InvalidDescription(val reason:String): ConfirmDetailsUiEvent()

}
