package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import com.example.cyclistance.feature_mapping_screen.domain.model.CardModel
import com.mapbox.geojson.Point


sealed class MappingEvent {

    object RequestHelp : MappingEvent()
    object RespondToHelp : MappingEvent()
    object SignOut: MappingEvent()
    object StartPinging: MappingEvent()
    object StopPinging: MappingEvent()
    object LoadUserProfile: MappingEvent()
    data class ChangeBottomSheet(val bottomSheetType: String): MappingEvent()
    object DismissAlertDialog: MappingEvent()
    object DismissNoInternetDialog: MappingEvent()
    object CancelRequestHelp: MappingEvent()
    data class DeclineRescueRequest(val cardModel: CardModel): MappingEvent()
    data class AcceptRescueRequest(val cardModel: CardModel): MappingEvent()
    object LoadData: MappingEvent()
    object CancelRescueTransaction: MappingEvent()
    data class ChangeCameraState(val cameraPosition: Point, val cameraZoomLevel: Double): MappingEvent()
    data class SelectRescueMapIcon(val id: String): MappingEvent()
    object DismissRescueeBanner: MappingEvent()
}
