package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state

import android.os.Parcelable
import com.mapbox.mapboxsdk.geometry.LatLng
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.core.utils.annotations.StableState
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteDirection
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class MappingUiState(
    val rescueRequestAccepted: Boolean = false,
    val requestHelpButtonVisible: Boolean = true,
    val searchingAssistance: Boolean = false,
    val isNoInternetVisible: Boolean = false,
    val mapSelectedRescuee: MapSelectedRescuee? = null,
    val routeDirection: RouteDirection? = null,
    val bottomSheetType: String? = null,
    val locationPermissionDialogVisible: Boolean = false,
    val isFabExpanded: Boolean = false,
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val isRescueRequestDialogVisible: Boolean = false,
    val isEmergencyCallDialogVisible: Boolean = false,
    val selectedPhoneNumber: String = "",
    val lastLongPressedLocation: LatLng? = null,
    val selectedIncidentLabel: String = "",
    val selectedHazardousMarker: HazardousLaneMarker? = HazardousLaneMarker(),
    val deleteHazardousMarkerDialogVisible: Boolean = false,
    val discardHazardousMarkerDialogVisible: Boolean = false,
    val currentlyEditingHazardousMarker: HazardousLaneMarker? = null,
    val hasTransaction: Boolean = false,
    val isRescueCancelled: Boolean = false,
    val isNavigating: Boolean = false,
    val generateRouteFailed: Boolean = false,
    val cancelSearchDialogVisible: Boolean = false,
    val cancelOnGoingRescueDialogVisible: Boolean = false,
    val notificationPermissionVisible: Boolean = false,
    val requestAcceptedVisible: Boolean = false,
    val requestCancelledVisible: Boolean = false,
    val banAccountDialogVisible: Boolean = false,



    ) : Parcelable