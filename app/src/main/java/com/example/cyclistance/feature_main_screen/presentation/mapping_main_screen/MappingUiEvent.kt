package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen


sealed class MappingUiEvent{
    object ShowNoInternetScreen: MappingUiEvent()
    object ShowConfirmDetailsScreen: MappingUiEvent()
    object ShowEditProfileScreen: MappingUiEvent()
    object ShowSignInScreen: MappingUiEvent()
    data class ShowToastMessage(val message: String): MappingUiEvent()
}
