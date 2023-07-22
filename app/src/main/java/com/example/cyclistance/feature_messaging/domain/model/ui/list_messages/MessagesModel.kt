package com.example.cyclistance.feature_messaging.domain.model.ui.list_messages

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class MessagesModel(
    val messages: List<MessageItemModel> = emptyList()
) : Parcelable
