package com.myapp.cyclistance.feature_emergency_call.domain.model

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class EmergencyCallModel(
    val contacts: List<EmergencyContactModel>
) : Parcelable{
    @StableState
    constructor(): this(
        contacts = emptyList()
    )
}

