package com.example.cyclistance.feature_messaging.presentation.chat.chats.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class ChatState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val userId: String? = null,
) : Parcelable
