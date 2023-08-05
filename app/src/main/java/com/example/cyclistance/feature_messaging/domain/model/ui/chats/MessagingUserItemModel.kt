package com.example.cyclistance.feature_messaging.domain.model.ui.chats

import android.os.Parcelable
import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class MessagingUserItemModel(
    val userDetails: UserDetails = UserDetails(),
    val fcmToken: String = "",
    val userAvailability: Boolean = false
) : Parcelable
