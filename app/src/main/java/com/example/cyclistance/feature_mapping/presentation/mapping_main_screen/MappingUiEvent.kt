package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen




sealed class MappingUiEvent{
    object ShowConfirmDetailsScreen: MappingUiEvent()
    object ShowEditProfileScreen: MappingUiEvent()
    object ShowSignInScreen: MappingUiEvent()
    object ShowMappingScreen: MappingUiEvent()
    data class ShowToastMessage(val message: String): MappingUiEvent()


}
