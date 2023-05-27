package com.example.cyclistance.feature_mapping.domain.model.location

import android.location.Address
import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class UserAddress(
    val address: Address? = null
) : Parcelable







