package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state

import android.os.Parcelable
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.MapSelectedRescuee
import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RouteDirection
import kotlinx.parcelize.Parcelize


@Parcelize
data class MappingUiState(
    val rescueRequestAccepted: Boolean= false,
    val requestHelpButtonVisible: Boolean= true,
    val searchingAssistance: Boolean= false,
    val isNoInternetVisible: Boolean= false,
    val mapSelectedRescuee: MapSelectedRescuee? = null,
    val routeDirection: RouteDirection? = null,
    val bottomSheetType:String = ""


): Parcelable
