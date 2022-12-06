package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import com.example.cyclistance.feature_mapping_screen.domain.model.CardModel
import com.mapbox.geojson.Point


sealed class MappingEvent {

    object SearchAssistance : MappingEvent()
    object SignOut: MappingEvent()
    object StartPinging: MappingEvent()
    object StopPinging: MappingEvent()
    object LoadUserProfile: MappingEvent()
    data class ChangeBottomSheet(val bottomSheetType: String): MappingEvent()
    object DismissAlertDialog: MappingEvent()
    object DismissNoInternetDialog: MappingEvent()
    object CancelSearchAssistance: MappingEvent()
    data class DeclineRescueRequest(val cardModel: CardModel): MappingEvent()
    data class AcceptRescueRequest(val cardModel: CardModel): MappingEvent()
    object LoadData: MappingEvent()
    object RemovedRescueTransaction: MappingEvent()
    data class ChangeCameraState(val cameraPosition: Point, val cameraZoomLevel: Double): MappingEvent()

}
