package com.myapp.cyclistance.navigation.state

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class NavState(
    val navigationStartingDestination:String? = null
) : Parcelable
