package com.myapp.cyclistance

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class MainState(
    val mappingIntentAction: String = ""
):Parcelable
