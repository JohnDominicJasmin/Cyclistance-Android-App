package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state

import android.os.Parcelable
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteDirection
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
import com.mapbox.mapboxsdk.geometry.LatLng
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
    val ableToEndRide: Boolean = false




    ) : Parcelable
