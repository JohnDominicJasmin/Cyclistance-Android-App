package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.location.Location


sealed class MappingEvent {
    object UploadProfile : MappingEvent()
    object SignOut: MappingEvent()
    data class ChangeBottomSheet(val bottomSheetType: String): MappingEvent()
    object LocationPermissionGranted: MappingEvent()
    data class OnLocationChange(val userLocation: Location): MappingEvent()
    object DismissNoInternetScreen: MappingEvent()
}
