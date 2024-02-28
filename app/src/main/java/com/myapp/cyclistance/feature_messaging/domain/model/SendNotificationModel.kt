package com.myapp.cyclistance.feature_messaging.domain.model

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class SendNotificationModel(
    val conversationId: String,
    val userReceiverToken: String,
    val senderName: String,
    val message: String,
):Parcelable{
    @StableState
    constructor(): this(
        conversationId = "",
        userReceiverToken = "",
        senderName = "",
        message = ""
    )
}
