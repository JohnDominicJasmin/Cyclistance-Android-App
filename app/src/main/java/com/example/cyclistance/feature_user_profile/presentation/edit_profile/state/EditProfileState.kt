package com.example.cyclistance.feature_user_profile.presentation.edit_profile.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class EditProfileState(
    val nameSnapshot: String = "",
    val cyclingGroupSnapshot: String = "",
    val addressSnapshot: String = "",
    val isLoading: Boolean = false,
    ):Parcelable