package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen


sealed class MappingEvent {
    object UploadProfile : MappingEvent()
    object SignOut: MappingEvent()
    object SubscribeToLocationUpdates: MappingEvent()
    object UnsubscribeToLocationUpdates: MappingEvent()
}
