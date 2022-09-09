package com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details


data class ConfirmDetailsState(
    val bikeType: String = "",
    val bikeTypeErrorMessage: String = "",
    val description: String = "",
    val descriptionErrorMessage: String = "",
    val message: String = "",
    val isLoading: Boolean = false,
    val address: String = "",
    val hasInternet: Boolean = true

)
