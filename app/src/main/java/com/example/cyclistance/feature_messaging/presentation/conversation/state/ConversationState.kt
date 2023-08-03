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
    val conversationAvailability: Boolean = false,
    val userUid: String = "",
    val userName: String = "",
    val userPhoto: String = "",
    val conversionId: String? = null,
    val isLoading: Boolean = false
) : Parcelable
