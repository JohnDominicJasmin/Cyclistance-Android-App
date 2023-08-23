package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue.NewRescueRequestsModel
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MapType
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class MappingState(
    val isLoading: Boolean = false,
    val userLocation: LocationModel? = null,
    val rescueTransaction: RescueTransactionItem? = null,
    val user: UserItem = UserItem(),
    val rescueRequestAcceptedUser: UserItem? = null,
    val rescuer: UserItem? = null,
    val rescuee: UserItem? = null,
    val profileUploaded: Boolean = false,
    val respondedToHelp: Boolean = false,
    val transactionLocation: LocationModel? = null,
    val rescuerETA: String = "",
    val rescuerDistance: String = "",
    val newRescueRequest: NewRescueRequestsModel? = null,
    val speedometerState: SpeedometerState = SpeedometerState(),
    val nearbyCyclist: NearbyCyclist? = null,
    val mapType: String = MapType.Default.type,
    val userId: String = ""
) : Parcelable{


    fun getTravelledDistance() = speedometerState.travelledDistance
    fun getTopSpeed() = speedometerState.topSpeed
    fun getCurrentSpeedKph() = speedometerState.currentSpeedKph


    fun getCurrentLocation() = user.location ?: userLocation
}