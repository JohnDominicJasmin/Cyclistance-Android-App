package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import android.location.Address
import androidx.compose.runtime.Immutable
import com.google.android.gms.maps.model.LatLng


@Immutable
data class UserAddress(
    val address: List<Address> = emptyList(),
)

data class MappingState(
    val isLoading: Boolean = false,
    val findAssistanceButtonVisible: Boolean = true,
    val userAddress: UserAddress = UserAddress(),
    val hasInternet: Boolean = true,
    val bottomSheetType: String = "",
    val locationPermissionGranted: Boolean = false,
)