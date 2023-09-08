package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.model.ui.camera.CameraState
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap

sealed class MappingUiEvent {

    // General Events
    data object RequestHelp : MappingUiEvent()
    data object RespondToHelp : MappingUiEvent()
    data object RescueArrivedConfirmed : MappingUiEvent()
    data object DestinationReachedConfirmed : MappingUiEvent()
    data object CancelSearching : MappingUiEvent()
    data object SearchCancelled : MappingUiEvent()
    data object ChatRescueTransaction : MappingUiEvent()
    data object CancelRescueTransaction : MappingUiEvent()
    data object CancelledRescueConfirmed : MappingUiEvent()
    data object DismissNoInternetDialog : MappingUiEvent()
    data object DismissEmergencyCallDialog : MappingUiEvent()
    data object DismissHazardousLaneMarkerDialog : MappingUiEvent()
    data object OnMapClick : MappingUiEvent()
    data object DismissBanner : MappingUiEvent()
    data object LocateUser : MappingUiEvent()
    data object RouteOverview : MappingUiEvent()
    data object RecenterRoute : MappingUiEvent()
    data object OpenNavigation : MappingUiEvent()
    data object OpenHazardousLaneBottomSheet : MappingUiEvent()
    data object OnRequestNavigationCameraToOverview : MappingUiEvent()
    data object DismissLocationPermission : MappingUiEvent()
    data object OnToggleExpandableFAB : MappingUiEvent()
    data object OnCollapseExpandableFAB : MappingUiEvent()
    data object ShowEmergencyCallDialog : MappingUiEvent()
    data object OpenFamilyTracker : MappingUiEvent()
    data object ShowRescueRequestDialog : MappingUiEvent()
    data object DismissRescueRequestDialog : MappingUiEvent()
    data object DismissRescueResultsDialog : MappingUiEvent()
    data object DismissSinoTrackWebView : MappingUiEvent()
    data object ShowSinoTrackWebView : MappingUiEvent()
    data object DismissAlertDialog : MappingUiEvent()
    data object DismissDiscardChangesMarkerDialog : MappingUiEvent()
    data object DismissIncidentDescriptionBottomSheet : MappingUiEvent()
    data object DiscardMarkerChanges : MappingUiEvent()
    data object CancelEditIncidentDescription : MappingUiEvent()
    data object OnAddEmergencyContact : MappingUiEvent()
    data object OnClickDeleteIncident : MappingUiEvent()
    data object OnConfirmDeleteIncident : MappingUiEvent()
    data object OnClickHazardousInfoGotIt : MappingUiEvent()
    data object DismissCancelSearchDialog : MappingUiEvent()
    data object RescueRequestAccepted: MappingUiEvent()

    // Events with Parameters
    data class OnInitializeMap(val mapboxMap: MapboxMap) : MappingUiEvent()
    data class OnMapLongClick(val latLng: LatLng) : MappingUiEvent()
    data class OnReportIncident(val labelIncident: String) : MappingUiEvent()
    data class OnEmergencyCall(val phoneNumber: String) : MappingUiEvent()
    data class OnSelectMapType(val mapType: String) : MappingUiEvent()
    data class DeclineRequestHelp(val id: String) : MappingUiEvent()
    data class ConfirmRequestHelp(val id: String) : MappingUiEvent()
    data class UpdateIncidentDescription(val label: String, val description: String) : MappingUiEvent()
    data class OnChangeIncidentDescription(val description: TextFieldValue) : MappingUiEvent()
    data class OnChangeIncidentLabel(val label: String) : MappingUiEvent()
    data class OnClickEditIncidentDescription(val marker: HazardousLaneMarker) : MappingUiEvent()
    data class OnClickMapMarker(val markerSnippet: String, val markerId: String) : MappingUiEvent()
    data class OnChangeCameraState(val cameraState: CameraState) : MappingUiEvent()
}
