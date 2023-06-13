package com.example.cyclistance.feature_message.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class MessagesModel(
    val messages: List<MessageItemModel> = emptyList()
) : Parcelable
