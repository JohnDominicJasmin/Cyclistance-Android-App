package com.example.cyclistance.feature_messaging.domain.model.ui.chats

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import java.util.Date


@StableState
@Parcelize
data class ChatItemModel(
    val messageId: String,

    val senderId: String,
    val receiverId: String,

    val conversionId: String,

    val lastMessage: String,
    val timeStamp: Date?,
    val isSent: Boolean,
    val isSeen: Boolean,


    ) : Parcelable {
    @StableState
    constructor() : this(
        messageId = "",
        senderId = "",
        receiverId = "",
        conversionId = "",
        lastMessage = "",
        timeStamp = null,
        isSent = false,
        isSeen = false,
    )
}
