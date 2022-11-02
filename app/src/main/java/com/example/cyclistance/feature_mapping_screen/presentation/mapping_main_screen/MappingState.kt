package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.NearbyCyclists
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.RescueRequestRespondents
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.UserAddress
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class MappingState(

    val isLoading: Boolean = false,
    val findAssistanceButtonVisible: Boolean = true,
    val hasInternet: Boolean = true,
    val bottomSheetType: String = "",
    val isSearchingForAssistance: Boolean = false,



    val user: UserItem = UserItem(),
    val rescueRequestRespondents: RescueRequestRespondents = RescueRequestRespondents(),
    val userAddress: UserAddress = UserAddress(),
    val currentAddress: String = "",
    val latitude: Double = DEFAULT_LATITUDE,
    val longitude: Double = DEFAULT_LONGITUDE,
    val name: String = "-----",
    val photoUrl: String = "",






    val nearbyCyclists: NearbyCyclists = NearbyCyclists(),



    ) : Parcelable