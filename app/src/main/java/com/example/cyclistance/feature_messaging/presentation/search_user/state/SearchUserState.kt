package com.example.cyclistance.feature_messaging.presentation.search_user.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class SearchUserState(
    val messagingUsers: MessagingUserModel = MessagingUserModel(),
    val isLoading: Boolean = false,

):Parcelable
