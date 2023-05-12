package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.event

sealed class ConfirmDetailsEvent{
    object ConfirmDetailsSuccess: ConfirmDetailsEvent()
    data class UserError(val reason:String = "User not found"): ConfirmDetailsEvent()
    data class UnexpectedError(val reason:String = "An unexpected error occurred."): ConfirmDetailsEvent()
    data class GetSavedBikeType(val bikeType: String): ConfirmDetailsEvent()
    data class GetSavedAddress(val address: String): ConfirmDetailsEvent()
    object NoInternetConnection: ConfirmDetailsEvent()
    data class InvalidAddress(val reason:String): ConfirmDetailsEvent()
    data class InvalidBikeType(val reason:String): ConfirmDetailsEvent()
    data class InvalidDescription(val reason:String): ConfirmDetailsEvent()

}
