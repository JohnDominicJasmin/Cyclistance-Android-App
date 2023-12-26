package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import androidx.compose.ui.text.input.TextFieldValue
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.domain.model.ui.camera.CameraState

sealed class MappingUiEvent {

    // General Events
    data object RequestHelp : MappingUiEvent()
    data object RespondToHelp : MappingUiEvent()
    data object CancelRespondHelp : MappingUiEvent()
    data object ConfirmedDestinationArrived : MappingUiEvent()
    data object ArrivedAtLocation : MappingUiEvent()
    data object CancelSearching : MappingUiEvent()
    data object SearchCancelled : MappingUiEvent()
    data object ChatRescueTransaction : MappingUiEvent()
    data object CancelRescueTransaction : MappingUiEvent()
    data object CancelledRescueConfirmed : MappingUiEvent()
    data object SelectImageFromGallery : MappingUiEvent()
    data object OpenCamera : MappingUiEvent()
    data object ViewImage : MappingUiEvent()
    data object ViewImageIncidentDetails : MappingUiEvent()



    data object OnMapClick : MappingUiEvent()
    data object DismissBanner : MappingUiEvent()
    data object LocateUser : MappingUiEvent()
    data object RouteOverview : MappingUiEvent()
    data object RecenterRoute : MappingUiEvent()
    data object OpenNavigation : MappingUiEvent()
    data object OpenSinoTrack : MappingUiEvent()
    data object OnRequestNavigationCameraToOverview : MappingUiEvent()

    data object OpenFamilyTracker : MappingUiEvent()

    data object DiscardMarkerChanges : MappingUiEvent()
    data object ToggleDefaultMapType : MappingUiEvent()
    data object ToggleTrafficMapType : MappingUiEvent()
    data object ToggleHazardousMapType : MappingUiEvent()
    data object CancelEditIncidentDescription : MappingUiEvent()
    data object OnAddEmergencyContact : MappingUiEvent()
    data object OnClickDeleteIncident : MappingUiEvent()
    data object OnConfirmDeleteIncident : MappingUiEvent()
    data object OnClickHazardousInfoGotIt : MappingUiEvent()
    data object DismissIncidentDescriptionBottomSheet : MappingUiEvent()
    data object DismissCallPhonePermissionDialog : MappingUiEvent()

    data object RescueRequestAccepted : MappingUiEvent()
    data object CancelOnGoingRescue : MappingUiEvent()
    data object ResetIncidentReport : MappingUiEvent()

    data object DismissProminentNotificationDialog : MappingUiEvent()
    data object DismissProminentGalleryDialog : MappingUiEvent()
    data object DismissProminentCameraDialog : MappingUiEvent()
    data object DismissProminentPhoneCallDialog : MappingUiEvent()
    data object DismissProminentLocationDialog : MappingUiEvent()

    data object AllowProminentNotificationDialog : MappingUiEvent()
    data object AllowProminentGalleryDialog : MappingUiEvent()
    data object AllowProminentCameraDialog : MappingUiEvent()
    data object AllowProminentPhoneCallDialog : MappingUiEvent()
    data object AllowProminentLocationDialog : MappingUiEvent()

    data class  MapTypeBottomSheet(val visibility: Boolean) : MappingUiEvent()

    // Events with Parameters
    data class AccessPhotoDialog(val visibility: Boolean) : MappingUiEvent()
    data class NotifyNewRescueRequest(val message: String) : MappingUiEvent()
    data class NotifyRequestAccepted(val message: String) : MappingUiEvent()
    data class HazardousLaneMarkerDialog(val visibility: Boolean) : MappingUiEvent()
    data class DiscardChangesMarkerDialog(val visibility: Boolean) : MappingUiEvent()
    data class CancelSearchDialog(val visibility: Boolean) : MappingUiEvent()
    data class CancelOnGoingRescueDialog(val visibility: Boolean) : MappingUiEvent()
    data class NoInternetDialog(val visibility: Boolean) : MappingUiEvent()
    data class ForegroundLocationPermissionDialog(val visibility: Boolean) : MappingUiEvent()
    data class BackgroundLocationPermissionDialog(val visibility: Boolean) : MappingUiEvent()
    data class CameraPermissionDialog(val visibility: Boolean) : MappingUiEvent()
    data class FilesAndMediaPermissionDialog(val visibility: Boolean) : MappingUiEvent()
    data class BannedAccountDialog(val visibility: Boolean) : MappingUiEvent()
    data class ExpandableFab(val expanded: Boolean) : MappingUiEvent()
    data class EmergencyCallDialog(val visibility: Boolean) : MappingUiEvent()
    data class RescueRequestDialog(val visibility: Boolean) : MappingUiEvent()
    data class AlertDialog(val alertDialogState: AlertDialogState = AlertDialogState()): MappingUiEvent()
    data class NotificationPermissionDialog(val visibility: Boolean) : MappingUiEvent()
    data class ReportIncidentDialog(val visibility: Boolean): MappingUiEvent()
    data class IncidentDescriptionDialog(val visibility: Boolean): MappingUiEvent()
    data class OnInitializeMap(val mapboxMap: MapboxMap) : MappingUiEvent()
    data class OnMapLongClick(val latLng: LatLng) : MappingUiEvent()
    data class OnReportIncident(val labelIncident: String) : MappingUiEvent()
    data class OnEmergencyCall(val phoneNumber: String) : MappingUiEvent()
    data class DeclineRequestHelp(val id: String) : MappingUiEvent()
    data class ConfirmRequestHelp(val id: String) : MappingUiEvent()
    data class ViewProfile(val id: String) : MappingUiEvent()
    data class UpdateIncidentDescription(val label: String, val description: String) :
        MappingUiEvent()

    data class OnChangeIncidentDescription(val description: TextFieldValue) : MappingUiEvent()
    data class OnChangeIncidentLabel(val label: String) : MappingUiEvent()
    data class OnClickEditIncidentDescription(val marker: HazardousLaneMarkerDetails) : MappingUiEvent()
    data class OnClickMapMarker(val markerSnippet: String, val markerId: String) : MappingUiEvent()
    data class OnChangeCameraState(val cameraState: CameraState) : MappingUiEvent()

}
