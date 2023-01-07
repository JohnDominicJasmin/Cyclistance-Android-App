package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details


sealed class ConfirmDetailsEvent{
    object ConfirmDetails: ConfirmDetailsEvent()
    data class SelectBikeType(val bikeType: String): ConfirmDetailsEvent()
    object ClearBikeTypeErrorMessage: ConfirmDetailsEvent()
    object ClearDescriptionErrorMessage: ConfirmDetailsEvent()
    data class SelectDescription(val description: String): ConfirmDetailsEvent()
    data class EnterMessage(val message: String): ConfirmDetailsEvent()
    data class EnterAddress(val address: String): ConfirmDetailsEvent()
    object DismissNoInternetDialog: ConfirmDetailsEvent()

}
