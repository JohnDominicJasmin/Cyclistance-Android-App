package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping_screen.domain.model.MappingBannerModel
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.CameraState
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.RescueRequestRespondents
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class MappingState(

//    UI STATES
    val isLoading: Boolean = false,
    val requestHelpButtonVisible: Boolean = true,
    val respondToHelpButtonVisible: Boolean = false,
    val hasInternet: Boolean = true,
    val bottomSheetType: String = "",
    val isSearchingForAssistance: Boolean = false,
    val alertDialogModel: AlertDialogModel = AlertDialogModel(),
    val cameraState: CameraState = CameraState(),
    val isNavigating : Boolean = false,
    val isRescueRequestAccepted: Boolean = false,







    val user: UserItem = UserItem(),
    val userRescueRequestRespondents: RescueRequestRespondents = RescueRequestRespondents(),
    val userRescueTransaction: RescueTransactionItem? = null,

    val userLocation: Location? = null,





    val rescuer: UserItem? = null,
    val rescuee: UserItem? = null,



    val respondedToHelp: Boolean = false,
    val transactionLocation: Location? = null,
    val rescuerETA: String = "",
    val currentAddress: String = "",
    val profileUploaded: Boolean = false,
    val name: String = "-----",
    val photoUrl: String = "",
    val selectedRescueeMapIcon: MappingBannerModel? = null,




    val nearbyCyclists: User? = null,






    ) : Parcelable