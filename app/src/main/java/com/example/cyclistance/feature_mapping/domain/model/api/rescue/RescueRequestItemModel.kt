package com.example.cyclistance.feature_mapping.domain.model.api.rescue

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RescueRequestItemModel(
    val id: String? = null,
    val name: String? = null,
    val profileImageUrl:String? = null,
    val estimatedTimeTravel: String? = null,
    val distance:String? = null,
    val address:String? = null,

    ):Parcelable
