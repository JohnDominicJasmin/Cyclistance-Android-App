package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen

import androidx.compose.runtime.Immutable
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.DEFAULT_LONGITUDE
import com.example.cyclistance.feature_mapping_screen.domain.model.User



@Immutable
data class Users(
    val activeUsers: List<User> = emptyList(),
)


data class MappingState(

    val isLoading: Boolean = false,
    val findAssistanceButtonVisible: Boolean = true,
    val currentAddress: String = "",
    val hasInternet: Boolean = true,
    val bottomSheetType: String = "",
    val isSearchingForAssistance: Boolean = false,
    val users: Users = Users(),
    val latitude: Double = DEFAULT_LATITUDE,
    val longitude: Double = DEFAULT_LONGITUDE,
)