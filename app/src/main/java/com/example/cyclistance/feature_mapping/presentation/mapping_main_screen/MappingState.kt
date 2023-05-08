package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping.domain.model.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.NewRescueRequestsModel
import com.example.cyclistance.feature_mapping.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.UserItem
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class MappingState(

    val isLoading: Boolean = false,
    val userLocation: Location? = null,
    val rescueTransaction: RescueTransactionItem? = null,
    val user: UserItem = UserItem(),
    val rescueRequestAcceptedUser: UserItem? = null,
    val rescuer: UserItem? = null,
    val rescuee: UserItem? = null,
    val profileUploaded: Boolean = false,
    val respondedToHelp: Boolean = false,
    val transactionLocation: Location? = null,
    val rescuerETA: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val nearbyCyclists: NearbyCyclist? = null,
    val newRescueRequest: NewRescueRequestsModel? = null,
//    val
    ) : Parcelable