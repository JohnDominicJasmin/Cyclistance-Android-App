package com.myapp.cyclistance.feature_messaging.domain.model

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class SendMessageModel(
    val receiverId: String,
    val message: String,
):Parcelable{
    @StableState
    constructor(): this(
        receiverId = "",
        message = ""
    )
}
