package com.example.cyclistance.feature_messaging.domain.model.ui.conversation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class MessageDuration : Parcelable {
    object OneHour : MessageDuration()
    object OneDay : MessageDuration()
    object OneWeek : MessageDuration()
    object OneMonth : MessageDuration()
    object OneYear : MessageDuration()


}
