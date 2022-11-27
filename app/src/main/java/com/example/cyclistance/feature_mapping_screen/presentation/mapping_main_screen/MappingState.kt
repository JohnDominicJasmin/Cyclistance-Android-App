package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.os.Parcelable
import android.view.View
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.CameraState
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.RescueRequestRespondents
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.UserAddress
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class MappingState(

    val isLoading: Boolean = false,
    val searchAssistanceButtonVisible: Boolean = true,
    val hasInternet: Boolean = true,
    val bottomSheetType: String = "",
    val isSearchingForAssistance: Boolean = false,
    val alertDialogModel: AlertDialogModel = AlertDialogModel(),
    val cameraState: CameraState = CameraState(),
    val tripProgressCardVisibility: Int = View.INVISIBLE,
    val maneuverViewVisibility: Int = View.INVISIBLE,
    val soundButtonVisibility: Int = View.INVISIBLE,
    val routeOverviewVisibility: Int = View.INVISIBLE,
    val recenterButtonVisibility: Int = View.INVISIBLE,


    val user: UserItem = UserItem(),
    val userRescueRequestRespondents: RescueRequestRespondents = RescueRequestRespondents(),
    val userAddress: UserAddress = UserAddress(),
    val userRescueTransaction: RescueTransactionItem? = null,

    val userLocation: Location = Location(
        latitude = DEFAULT_LATITUDE,
        longitude = DEFAULT_LONGITUDE
    ),
    val rescuer: UserItem = UserItem(),

    val transactionLocation: Location? = null,
    val rescuerETA: String = "",
    val currentAddress: String = "",
    val name: String = "-----",
    val photoUrl: String = "",


    val nearbyCyclists: User = User(),


    ) : Parcelable