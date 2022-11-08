package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import com.example.cyclistance.feature_mapping_screen.domain.model.CardModel


sealed class MappingEvent {

    object SearchAssistance : MappingEvent()
    object SignOut: MappingEvent()
    object StartPinging: MappingEvent()
    object StopPinging: MappingEvent()
    object SubscribeToNearbyUsersChanges: MappingEvent()
    object UnsubscribeToNearbyUsersChanges: MappingEvent()
    object LoadUserImageLocationPuck: MappingEvent()
    object LoadUserProfile: MappingEvent()
    object PostLocation: MappingEvent()
    data class ChangeBottomSheet(val bottomSheetType: String): MappingEvent()
    object SubscribeToLocationUpdates: MappingEvent()
    object UnsubscribeToLocationUpdates: MappingEvent()
    object DismissNoInternetScreen: MappingEvent()
    object CancelSearchAssistance: MappingEvent()
    data class DeclineRescueRequest(val cardModel: CardModel): MappingEvent()
    data class AcceptRescueRequest(val cardModel: CardModel): MappingEvent()
    object SubscribeToRescueTransactionChanges: MappingEvent()
    object UnsubscribeToRescueTransactionChanges: MappingEvent()

}
