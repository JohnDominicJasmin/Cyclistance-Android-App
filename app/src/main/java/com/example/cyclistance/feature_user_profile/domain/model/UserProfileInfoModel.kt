package com.example.cyclistance.feature_user_profile.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class UserProfileInfoModel(
    val photoUrl: String,
    val name: String,
    val averageRating: Double,
    val address: String,
    val bikeGroup: String,
):Parcelable{
    @StableState
    constructor(): this(
        photoUrl = "",
        name = "",
        averageRating = 0.0,
        address = "",
        bikeGroup = ""
    )
}


