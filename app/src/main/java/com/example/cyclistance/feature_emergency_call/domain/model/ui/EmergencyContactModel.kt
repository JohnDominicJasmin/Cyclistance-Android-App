package com.example.cyclistance.feature_emergency_call.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class EmergencyContactModel(
    val id: String = "",
    val name: String = "",
    val photo: String = "",
    val number: String = ""
) : Parcelable
