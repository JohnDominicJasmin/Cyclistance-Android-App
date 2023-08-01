package com.example.cyclistance.feature_messaging.domain.model.ui.chats

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class ChatsModel(
    val chats: List<ChatItemModel> = emptyList()
) : Parcelable
