package com.example.cyclistance.navigation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class NavUiState(
    val internetAvailable: Boolean = false,
    val isNavigating: Boolean = false,
    val photoUrl: String = "",
    val name: String = "",
):Parcelable