package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.event

import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapboxMap

sealed class MappingUiEvent{
    object OnClickRequestHelp: MappingUiEvent()
    object OnClickRespondToHelp: MappingUiEvent()
    object OnClickRescueArrived: MappingUiEvent()
    object OnClickDestinationReached: MappingUiEvent()
    object OnClickCancelSearch: MappingUiEvent()
    object OnClickCallRescueTransaction: MappingUiEvent()
    object OnClickChatRescueTransaction: MappingUiEvent()
    object OnClickCancelRescueTransaction: MappingUiEvent()
    object OnClickOkCancelledRescue: MappingUiEvent()
    data class OnInitializeMap(val mapboxMap: MapboxMap): MappingUiEvent()
    object OnClickOkRescueRequestAccepted: MappingUiEvent()
    data class OnChangeCameraState(val position: LatLng, val zoomLevel: Double): MappingUiEvent()
    object DismissNoInternetDialog : MappingUiEvent()
    data class OnClickRescueeMapIcon(val id: String): MappingUiEvent()
    object OnMapClick: MappingUiEvent()
    object OnClickDismissBanner: MappingUiEvent()
    object OnClickLocate: MappingUiEvent()
    object OnClickRouteOverviewButton: MappingUiEvent()
    object OnClickRecenterButton: MappingUiEvent()
    object OnClickOpenNavigationButton: MappingUiEvent()
    object OnRequestNavigationCameraToOverview: MappingUiEvent()





}
