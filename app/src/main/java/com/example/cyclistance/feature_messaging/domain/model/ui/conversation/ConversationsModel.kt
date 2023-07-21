package com.example.cyclistance.feature_messaging.domain.model.ui.conversation

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class ConversationsModel(
    val messages: List<ConversationItemModel> = emptyList()
) : Parcelable
