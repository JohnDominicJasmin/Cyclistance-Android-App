package com.example.cyclistance.feature_messaging.domain.model.ui.conversation

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class ConversationItemModel(
    val messageId: String,
    val senderId: String,
    val recipientId: String,
    val message: String,
    val dateSent: String? = null,
    val messageDuration: MessageDuration? = null,
) : Parcelable

