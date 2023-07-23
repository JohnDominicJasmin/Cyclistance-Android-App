package com.example.cyclistance.feature_messaging.domain.model.ui.conversation

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import java.util.Date


@StableState
@Parcelize
data class ConversationItemModel(
    val messageId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timeStamp: Date? = null,
    val messageDuration: MessageDuration? = null,
) : Parcelable

