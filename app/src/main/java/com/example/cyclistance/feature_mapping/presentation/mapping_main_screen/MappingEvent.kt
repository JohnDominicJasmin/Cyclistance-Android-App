package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import com.example.cyclistance.feature_mapping.domain.model.CardModel
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng


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
    object SubscribeToDataChanges: MappingEvent()
    object CancelRescueTransaction: MappingEvent()
    data class ChangeCameraState(val cameraPosition: LatLng, val cameraZoomLevel: Double): MappingEvent()
    data class SelectRescueMapIcon(val id: String): MappingEvent()
    object DismissRescueeBanner: MappingEvent()
    object DismissRequestAccepted: MappingEvent()
    object StartNavigation: MappingEvent()
    object StopNavigation: MappingEvent()
    data class ShowRouteDirections(val origin: Point, val destination: Point): MappingEvent()
    object RemoveRouteDirections: MappingEvent()


}