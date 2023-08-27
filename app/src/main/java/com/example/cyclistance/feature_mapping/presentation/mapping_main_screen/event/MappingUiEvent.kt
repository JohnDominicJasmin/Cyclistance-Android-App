package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.model.ui.camera.CameraState
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap

sealed class MappingUiEvent{
    object RequestHelp: MappingUiEvent()
    object RespondToHelp: MappingUiEvent()
    object RescueArrivedConfirmed: MappingUiEvent()
    object DestinationReachedConfirmed: MappingUiEvent()
    object CancelSearchConfirmed: MappingUiEvent()
    object CallRescueTransaction: MappingUiEvent()
    object ChatRescueTransaction: MappingUiEvent()
    object CancelRescueTransaction: MappingUiEvent()
    object CancelledRescueConfirmed: MappingUiEvent()
    data class OnInitializeMap(val mapboxMap: MapboxMap): MappingUiEvent()
    object RescueRequestAccepted: MappingUiEvent()
    data class OnChangeCameraState(val cameraState: CameraState) : MappingUiEvent()
    object DismissNoInternetDialog : MappingUiEvent()
    object DismissEmergencyCallDialog : MappingUiEvent()
    object DismissHazardousLaneMarkerDialog : MappingUiEvent()
    object OnMapClick : MappingUiEvent()
    data class OnMapLongClick(val latLng: LatLng) : MappingUiEvent()
    object DismissBanner : MappingUiEvent()
    object LocateUser : MappingUiEvent()
    object RouteOverview : MappingUiEvent()
    object RecenterRoute : MappingUiEvent()
    object OpenNavigation : MappingUiEvent()
    object OpenHazardousLaneBottomSheet : MappingUiEvent()
    object OnRequestNavigationCameraToOverview : MappingUiEvent()
    object DismissLocationPermission : MappingUiEvent()
    object DismissPhonePermission : MappingUiEvent()
    data class OnReportIncident(val labelIncident: String) : MappingUiEvent()
    object OnToggleExpandableFAB : MappingUiEvent()
    object OnCollapseExpandableFAB : MappingUiEvent()

    object ShowEmergencyCallDialog : MappingUiEvent()
    object OpenFamilyTracker : MappingUiEvent()
    object ShowRescueRequestDialog : MappingUiEvent()
    object DismissRescueRequestDialog : MappingUiEvent()
    object DismissRescueResultsDialog : MappingUiEvent()
    object DismissSinoTrackWebView : MappingUiEvent()
    object ShowSinoTrackWebView : MappingUiEvent()
    data class OnEmergencyCall(val phoneNumber: String) : MappingUiEvent()
    data class OnSelectMapType(val mapType: String) : MappingUiEvent()


    data class CancelRequestHelp(val id: String) : MappingUiEvent()
    data class ConfirmRequestHelp(val id: String) : MappingUiEvent()
    object DismissAlertDialog : MappingUiEvent()
    object DismissDiscardChangesMarkerDialog: MappingUiEvent()
    object DismissIncidentDescriptionBottomSheet: MappingUiEvent()
    object DiscardMarkerChanges: MappingUiEvent()
    object CancelEditIncidentDescription: MappingUiEvent()

    object OnAddEmergencyContact : MappingUiEvent()

    data class OnChangeIncidentDescription(val description: TextFieldValue) : MappingUiEvent()
    data class OnChangeIncidentLabel(val label: String) : MappingUiEvent()

    data class OnClickEditIncidentDescription(val marker: HazardousLaneMarker) : MappingUiEvent()
    object OnClickDeleteIncident : MappingUiEvent()
    object OnConfirmDeleteIncident: MappingUiEvent()

    data class OnClickMapMarker(val markerSnippet: String, val markerId: String): MappingUiEvent()

}
