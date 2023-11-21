package com.myapp.cyclistance.feature_messaging.domain.model.ui.conversation

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class ConversationItemModel(
    val messageId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: String,
    val messageDuration: MessageDuration?,
    val isSent : Boolean,
    val isSeen: Boolean
) : Parcelable{

    @StableState
    constructor(): this(
        messageId = "",
        senderId = "",
        receiverId = "",
        message = "",
        timestamp = "",
        messageDuration = null,
        isSent = false,
        isSeen = false
    )

}


