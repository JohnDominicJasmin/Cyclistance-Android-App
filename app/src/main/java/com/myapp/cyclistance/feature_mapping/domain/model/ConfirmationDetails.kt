package com.myapp.cyclistance.feature_mapping.domain.model

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class ConfirmationDetails(
    val address: String,
    val bikeType: String,
    val description: String,
    val message: String,
) : Parcelable{
    @StableState
    constructor() : this(
        address = "",
        bikeType = "",
        description = "",
        message = "",
    )
}
