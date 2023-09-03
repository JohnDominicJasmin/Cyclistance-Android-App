package com.example.cyclistance.feature_user_profile.presentation.user_profile.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class UserProfileState(
    val isLoading: Boolean = false,
    val userProfileModel: UserProfileModel = UserProfileModel(),
    val userId: String = "",
    val profileSelectedId: String = "",

    val userReceiverMessage: MessagingUserItemModel? = null,
    val userSenderMessage: MessagingUserItemModel? = null,
):Parcelable
