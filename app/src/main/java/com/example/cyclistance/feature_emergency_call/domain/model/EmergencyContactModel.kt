package com.example.cyclistance.feature_emergency_call.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class EmergencyContactModel(
    val id: Int,
    val name: String,
    val photo: String,
    val phoneNumber: String
) : Parcelable {
    @StableState
    constructor() : this(
        id = 0,
        name = "",
        photo = "",
        phoneNumber = "")

    @StableState
    constructor(name: String, photo: String, phoneNumber: String) : this(
        id = 0,
        name = name,
        photo = photo,
        phoneNumber = phoneNumber)
    fun isEmpty(): Boolean {
        return name.isEmpty() && photo.isEmpty() && phoneNumber.isEmpty()
    }
}
