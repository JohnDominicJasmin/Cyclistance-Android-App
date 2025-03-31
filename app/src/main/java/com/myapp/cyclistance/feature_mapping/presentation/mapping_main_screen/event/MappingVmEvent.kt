package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.geometry.LatLng
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee


sealed class MappingVmEvent {

    data object RequestHelp : MappingVmEvent()
    data class RespondToHelp(val selectedRescuee: MapSelectedRescuee) : MappingVmEvent()
    data class CancelRespondHelp(val id: String) : MappingVmEvent()
    data class SelectRescueMapIcon(val id: String): MappingVmEvent()
    data object CancelSearchingAssistance: MappingVmEvent()
    data class DeclineRescueRequest(val id: String): MappingVmEvent()
    data class AcceptRescueRequest(val id: String): MappingVmEvent()
    data object CancelRescueTransaction: MappingVmEvent()
    data object DestinationArrived: MappingVmEvent()
    data class GetRouteDirections(val origin: Point, val destination: Point): MappingVmEvent()
    data class ReportIncident(val label: String, val latLng: LatLng, val description: String, val imageUri: String): MappingVmEvent()
    data class UpdateReportedIncident(val marker: HazardousLaneMarkerDetails): MappingVmEvent()
    data class SelectHazardousLaneMarker(val id: String): MappingVmEvent()
    data class DeleteHazardousLaneMarker(val id: String): MappingVmEvent()
    data class ShouldShowHazardousStartingInfo(val shouldShow: Boolean): MappingVmEvent()
    data class NotifyNewRescueRequest(val message: String): MappingVmEvent()
    data class NotifyRequestAccepted(val message: String): MappingVmEvent()
    data object ArrivedAtLocation: MappingVmEvent()
    data object ToggleDefaultMapType: MappingVmEvent()
    data object ToggleTrafficMapType: MappingVmEvent()
    data object ToggleHazardousMapType: MappingVmEvent()

}
