package com.example.cyclistance.feature_messaging.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class MessageItemModel(
    val messageId: String = "",
    val userPhotoUrl: String = "",
    val name: String = "",
    val message: String = "",
    val timeStamp: String = "",
    val unreadMessages: Int = 0,
    val isMessageSent: Boolean = false,

    ) : Parcelable
