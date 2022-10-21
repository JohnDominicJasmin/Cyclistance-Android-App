package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen


sealed class MappingEvent {
    object SearchAssistance : MappingEvent()
    object SignOut: MappingEvent()
    object StartPinging: MappingEvent()
    object StopPinging: MappingEvent()
    object LoadUsers: MappingEvent()
    object LoadUserProfile: MappingEvent()
    data class ChangeBottomSheet(val bottomSheetType: String): MappingEvent()
    object SubscribeToLocationUpdates: MappingEvent()
    object UnsubscribeToLocationUpdates: MappingEvent()
    object DismissNoInternetScreen: MappingEvent()
    object CancelSearchAssistance: MappingEvent()

}
