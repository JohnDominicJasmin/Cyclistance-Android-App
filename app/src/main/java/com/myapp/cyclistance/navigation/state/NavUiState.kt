package com.myapp.cyclistance.navigation.state

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class NavUiState(
    val internetAvailable: Boolean = true,
    val isNavigating: Boolean = false,
    val startingDestination: String = "",
    val drawerPhotoUrl: String? = null,
    val drawerDisplayName: String? = null,
    val emergencyContactOnEditMode: Boolean = false,
):Parcelable
