package com.example.cyclistance.feature_messaging.presentation.conversation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ConversationState(
    val conversationName: String = "",
    val conversationUid: String = "",
    val conversationPhotoUrl: String = "",
    val userUid: String = ""
) : Parcelable
