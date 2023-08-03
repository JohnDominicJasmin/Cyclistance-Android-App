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


    val conversionPhoto: String = "",
    val conversionName: String = "",
    val conversionId: String = "",
    val isUserAvailable: Boolean = false,

    val lastMessage: String = "",
    val timeStamp: Date? = null,


    ) : Parcelable
