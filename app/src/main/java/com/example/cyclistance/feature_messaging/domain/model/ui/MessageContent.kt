package com.example.cyclistance.feature_messaging.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class MessageContent(
    val messageId: String,
    val senderId: String,
    val recipientId: String,
    val content: String,
    val dateSent: String? = null,
    val duration: Duration? = null,
) : Parcelable

@Parcelize
sealed class Duration : Parcelable {
    object OneHour : Duration()
    object OneDay : Duration()
    object OneWeek : Duration()
    object OneMonth : Duration()
    object OneYear : Duration()


}