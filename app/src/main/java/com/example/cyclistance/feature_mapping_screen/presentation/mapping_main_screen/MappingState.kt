package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import android.location.Address
import androidx.compose.runtime.Immutable
import com.example.cyclistance.feature_mapping_screen.domain.model.User


@Immutable
data class UserAddress(
    val address: List<Address> = emptyList(),
)

@Immutable
data class Users(
    val users: List<User> = emptyList(),
)


    data class MappingState(
    val isLoading: Boolean = false,
    val findAssistanceButtonVisible: Boolean = true,
    val userAddress: UserAddress = UserAddress(),
    val currentAddress: String = "",
    val hasInternet: Boolean = true,
    val bottomSheetType: String = "",
    val locationPermissionGranted: Boolean = false,
    val isSearchingForAssistance: Boolean = false,
    val users: Users = Users()
)