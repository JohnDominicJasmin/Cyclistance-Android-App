package com.example.cyclistance.navigation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class NavState(
    val navigationStartingDestination:String? = null
) : Parcelable
