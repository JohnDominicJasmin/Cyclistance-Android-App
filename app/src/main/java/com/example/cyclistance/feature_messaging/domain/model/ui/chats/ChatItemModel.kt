package com.example.cyclistance.feature_messaging.domain.model.ui.chats

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import java.util.Date



@StableState
@Parcelize
data class ChatItemModel(
    val messageId: String = "",

    val senderId: String = "",
    val receiverId: String = "",

    val conversionId: String = "",

    val lastMessage: String = "",
    val timeStamp: Date? = null,
    val isSent: Boolean = false,
    val isSeen: Boolean = false,


    ) : Parcelable
