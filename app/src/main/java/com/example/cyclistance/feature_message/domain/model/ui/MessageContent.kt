package com.example.cyclistance.feature_message.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class MessageContent(
    val senderId: String,
    val recipientId: String,
    val content: String,
    val dateSent: String,
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