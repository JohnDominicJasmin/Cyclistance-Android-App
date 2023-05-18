package com.example.cyclistance.navigation.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class NavState(
    val navigationStartingDestination:String? = null
) : Parcelable
