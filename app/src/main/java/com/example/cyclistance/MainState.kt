package com.example.cyclistance

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class MainState(
    val mappingIntentAction: String = ""
):Parcelable
