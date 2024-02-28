package com.myapp.cyclistance.feature_user_profile.presentation.user_profile.state

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import com.myapp.cyclistance.feature_user_profile.domain.model.UserProfileModel
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class UserProfileState(
    val isLoading: Boolean = false,
    val userProfileModel: UserProfileModel = UserProfileModel(),
    val userId: String = "",
    val profileSelectedId: String = "",

):Parcelable
