package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.myapp.cyclistance.feature_mapping.domain.model.ui.rescue.NewRescueRequestsModel
import com.myapp.cyclistance.feature_report_account.domain.model.BannedAccountDetails
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class MappingState(
    val isLoading: Boolean = false,
    val userLocation: LocationModel? = null,
    val rescueTransaction: RescueTransactionItem? = null,
    val user: UserItem = UserItem(),
    val rescuer: UserItem? = null,
    val rescuee: UserItem? = null,
    val profileUploaded: Boolean = false,
    val respondedToHelp: Boolean = false,
    val transactionLocation: LocationModel? = null,
    val rescueETA: String = "",
    val rescueDistance: Double? = null,
    val newRescueRequest: NewRescueRequestsModel? = null,
    val speedometerState: SpeedometerState = SpeedometerState(),
    val nearbyCyclist: NearbyCyclist? = null,
    val defaultMapTypeSelected: Boolean = true,
    val hazardousMapTypeSelected: Boolean = false,
    val trafficMapTypeSelected: Boolean = false,
    val userId: String = "",
    val shouldShowHazardousStartingInfo: Boolean = false,
    val lastRequestNotifiedId: String = "",
    val bannedAccountDetails: BannedAccountDetails? = null
) : Parcelable{


    fun getTransactionId() = user.getTransactionId() ?: rescueTransaction?.id ?: ""
    fun getTopSpeed() = speedometerState.topSpeed

    fun getCurrentLocation() = user.location ?: userLocation
}