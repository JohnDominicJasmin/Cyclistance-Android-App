package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import com.example.cyclistance.feature_mapping.domain.model.MapSelectedRescuee
import com.mapbox.geojson.Point


sealed class MappingEvent {

    object RequestHelp : MappingEvent()
    data class RespondToHelp(val selectedRescuee: MapSelectedRescuee) : MappingEvent()
    data class SelectRescueMapIcon(val id: String): MappingEvent()
    /**
     * Available BottomSheetType:
     * RescuerArrived,DestinationReached, SearchAssistance, OnGoingRescue
     * Usage: DestinationReached.type
     * */
    object CancelRequestHelp: MappingEvent()
    data class DeclineRescueRequest(val id: String): MappingEvent()
    data class AcceptRescueRequest(val id: String): MappingEvent()
    object LoadData: MappingEvent()
    object SubscribeToDataChanges: MappingEvent()
    object CancelRescueTransaction: MappingEvent()
    data class GetRouteDirections(val origin: Point, val destination: Point): MappingEvent()


}
