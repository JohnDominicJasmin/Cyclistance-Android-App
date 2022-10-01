package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen


sealed class MappingEvent {
    object UploadProfile : MappingEvent()
    object SignOut: MappingEvent()
    data class ChangeBottomSheet(val bottomSheetType: String): MappingEvent()
    object LocationPermissionGranted: MappingEvent()
    object DismissNoInternetScreen: MappingEvent()
}
