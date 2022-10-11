package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthEvent


sealed class MappingEvent {
    object UploadProfile : MappingEvent()
    object SignOut: MappingEvent()
    object StartPinging: MappingEvent()
    object StopPinging: MappingEvent()
    object GetUsersAsynchronously: MappingEvent()
    data class ChangeBottomSheet(val bottomSheetType: String): MappingEvent()
    object SubscribeToLocationUpdates: MappingEvent()
    object UnsubscribeToLocationUpdates: MappingEvent()
    object DismissNoInternetScreen: MappingEvent()

}
