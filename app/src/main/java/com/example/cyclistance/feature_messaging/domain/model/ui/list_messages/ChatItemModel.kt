package com.example.cyclistance.feature_messaging.domain.model.ui.list_messages

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import java.util.Date


@StableState
@Parcelize
data class ChatItemModel(
    val messageId: String = "",
    val userPhotoUrl: String = "",
    val name: String = "",
    val userId: String = "",
    val message: String = "",
    val timeStamp: Date? = null,


    ) : Parcelable
