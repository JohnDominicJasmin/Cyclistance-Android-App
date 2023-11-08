package com.example.cyclistance.core.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.core.utils.constants.MappingConstants
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class UserDetails(
    val uid: String,
    val name: String,
    val photo: String,
    val email: String,
) : Parcelable {

    @StableState
    constructor() : this(
        uid = "",
        name = "",
        photo = MappingConstants.IMAGE_PLACEHOLDER_URL,
        email = "",
    )
}
