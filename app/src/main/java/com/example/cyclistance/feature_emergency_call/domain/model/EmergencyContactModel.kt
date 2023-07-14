package com.example.cyclistance.feature_emergency_call.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class EmergencyContactModel(
    val id: Int = 0,
    val name: String = "",
    val photo: String = "",
    val phoneNumber: String = ""
) : Parcelable {
    fun isEmpty(): Boolean {
        return name.isEmpty() && photo.isEmpty() && phoneNumber.isEmpty()
    }
}
