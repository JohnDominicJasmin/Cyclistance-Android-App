package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.graphics.Bitmap
import android.location.Address
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class Cyclist(
    val user: User = User(),
    val profileImageBitmap: Bitmap? = null,
):Parcelable

@Parcelize
@Immutable
@Stable
data class NearbyCyclists(
    val activeUsers: List<Cyclist> = emptyList(),
):Parcelable




@Parcelize
@Immutable
@Stable
data class RescueRequestRespondents(
    val respondents: List<User> = emptyList(),
):Parcelable

@Parcelize
@Immutable
@Stable
data class UserAddress(
    val address: Address? = null
):Parcelable


@Parcelize
@Immutable
@Stable
data class MappingState(

    val isLoading: Boolean = false,
    val findAssistanceButtonVisible: Boolean = true,
    val currentAddress: String = "",
    val hasInternet: Boolean = true,
    val bottomSheetType: String = "",
    val isSearchingForAssistance: Boolean = false,

    val latitude: Double = DEFAULT_LATITUDE,
    val longitude: Double = DEFAULT_LONGITUDE,
    val name: String = "-----",
    val photoUrl : String = "",

    val nearbyCyclists: NearbyCyclists = NearbyCyclists(),
    val user: User = User(),
    val rescueRequestRespondents: RescueRequestRespondents = RescueRequestRespondents(),
    val userAddress: UserAddress = UserAddress(),



):Parcelable