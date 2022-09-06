package com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details


sealed class ConfirmDetailsEvent{
    object Save: ConfirmDetailsEvent()
    data class SelectBikeType(val bikeType: String): ConfirmDetailsEvent()
    data class SelectDescription(val description: String): ConfirmDetailsEvent()
    data class EnterMessage(val message: String): ConfirmDetailsEvent()
    data class EnterAddress(val address: String): ConfirmDetailsEvent()


}
