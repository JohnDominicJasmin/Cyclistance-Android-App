package com.example.cyclistance.feature_messaging.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class MessageConversation(
    val messages: List<MessageContent> = emptyList()
) : Parcelable
