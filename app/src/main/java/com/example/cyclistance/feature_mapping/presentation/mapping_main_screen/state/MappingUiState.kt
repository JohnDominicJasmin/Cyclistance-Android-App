package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RouteDirection
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
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
    val phonePermissionDialogVisible: Boolean = false,
    val isFabExpanded: Boolean = false,
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val isRescueRequestDialogVisible: Boolean = false,
    val isEmergencyCallDialogVisible: Boolean = false,


    ) : Parcelable
