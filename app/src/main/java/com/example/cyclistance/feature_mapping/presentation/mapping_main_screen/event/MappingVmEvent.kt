package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng


sealed class MappingVmEvent {

    data object RequestHelp : MappingVmEvent()
    data class RespondToHelp(val selectedRescuee: MapSelectedRescuee) : MappingVmEvent()
    data class SelectRescueMapIcon(val id: String): MappingVmEvent()
    /**
     * Available BottomSheetType:
     * RescuerArrived,DestinationReached, SearchAssistance, OnGoingRescue
     * Usage: DestinationReached.type
     * */
    data object CancelSearchingAssistance: MappingVmEvent()
    data class DeclineRescueRequest(val id: String): MappingVmEvent()
    data class AcceptRescueRequest(val id: String): MappingVmEvent()
    data object LoadData: MappingVmEvent()
    data object SubscribeToDataChanges: MappingVmEvent()
    data object CancelRescueTransaction: MappingVmEvent()
    data class GetRouteDirections(val origin: Point, val destination: Point): MappingVmEvent()
    data class ReportIncident(val label: String, val latLng: LatLng, val description: String): MappingVmEvent()
    data class UpdateReportedIncident(val marker: HazardousLaneMarker): MappingVmEvent()
    data class SetMapType(val mapType: String): MappingVmEvent()
    data class SelectHazardousLaneMarker(val id: String): MappingVmEvent()
    data class DeleteHazardousLaneMarker(val id: String): MappingVmEvent()
    data class ShouldShowHazardousStartingInfo(val shouldShow: Boolean): MappingVmEvent()
    data class NotifyUser(val title: String, val message: String): MappingVmEvent()

}
