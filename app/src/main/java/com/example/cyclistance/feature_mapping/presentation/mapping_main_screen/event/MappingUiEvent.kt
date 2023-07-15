package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import com.example.cyclistance.feature_mapping.domain.model.ui.camera.CameraState
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
    data class RescueeMapIconSelected(val id: String) : MappingUiEvent()
    object OnMapClick : MappingUiEvent()
    object OnMapLongClick : MappingUiEvent()
    object DismissBanner : MappingUiEvent()
    object LocateUser : MappingUiEvent()
    object RouteOverview : MappingUiEvent()
    object RecenterRoute : MappingUiEvent()
    object OpenNavigation : MappingUiEvent()
    object OnRequestNavigationCameraToOverview : MappingUiEvent()
    object DismissLocationPermission : MappingUiEvent()
    object DismissPhonePermission : MappingUiEvent()
    data class OnReportIncident(val incident: String) : MappingUiEvent()
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


    data class CancelRequestHelp(val id: String) : MappingUiEvent()
    data class ConfirmRequestHelp(val id: String) : MappingUiEvent()
    object DismissAlertDialog : MappingUiEvent()


}
