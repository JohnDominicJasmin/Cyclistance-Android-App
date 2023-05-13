package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import com.example.cyclistance.feature_mapping.domain.model.MapSelectedRescuee
import com.mapbox.geojson.Point


sealed class MappingVmEvent {

    object RequestHelp : MappingVmEvent()
    data class RespondToHelp(val selectedRescuee: MapSelectedRescuee) : MappingVmEvent()
    data class SelectRescueMapIcon(val id: String): MappingVmEvent()
    /**
     * Available BottomSheetType:
     * RescuerArrived,DestinationReached, SearchAssistance, OnGoingRescue
     * Usage: DestinationReached.type
     * */
    object CancelRequestHelp: MappingVmEvent()
    data class DeclineRescueRequest(val id: String): MappingVmEvent()
    data class AcceptRescueRequest(val id: String): MappingVmEvent()
    object LoadData: MappingVmEvent()
    object SubscribeToDataChanges: MappingVmEvent()
    object CancelRescueTransaction: MappingVmEvent()
    data class GetRouteDirections(val origin: Point, val destination: Point): MappingVmEvent()


}
