package com.example.cyclistance.feature_user_profile.presentation.user_profile.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class UserProfileState(
    val userProfileModel: UserProfileModel = UserProfileModel(),
):Parcelable