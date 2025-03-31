package com.myapp.cyclistance.navigation.state

import android.os.Parcelable
import com.myapp.cyclistance.core.network.ConnectivityObserver
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class NavUiState(
    val isNavigating: Boolean = false,
    val internetStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val startingDestination: String = "",
    val drawerPhotoUrl: String? = null,
    val drawerDisplayName: String? = null,
    val emergencyContactOnEditMode: Boolean = false,
):Parcelable
