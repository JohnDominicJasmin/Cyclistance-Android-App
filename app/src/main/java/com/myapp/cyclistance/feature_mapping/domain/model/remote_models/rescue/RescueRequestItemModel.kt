package com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class RescueRequestItemModel(
    val id: String? = null,
    val name: String? = null,
    val profileImageUrl:String? = null,
    val estimatedTimeTravel: String? = null,
    val distance:String? = null,
    val address:String? = null,
    val timestamp: Long = 0L,

    ):Parcelable
