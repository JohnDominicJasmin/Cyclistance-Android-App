package com.example.cyclistance.feature_mapping.domain.model.ui.rescue

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class CancelledRescueModel(
    val transactionID:String? = null,
    val rescueCancelledBy:String? = null,
    val reason:String? = null,
    val message:String? = null
):Parcelable
