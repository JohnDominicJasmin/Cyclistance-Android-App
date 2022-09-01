package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import androidx.annotation.RawRes

sealed class MappingUiEvent{
    object ShowNoInternetScreen: MappingUiEvent()
    object ShowConfirmDetailsScreen: MappingUiEvent()
    object ShowEditProfileScreen: MappingUiEvent()
    object ShowSignInScreen: MappingUiEvent()
    data class ShowToastMessage(val message: String): MappingUiEvent()

    data class ShowAlertDialog(
        val title: String = "",
        val description: String = "",
        @RawRes val imageResId: Int = -1
    ): MappingUiEvent()

}
