package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class EditProfileState(
    val nameSnapshot: String = "",
    val isLoading: Boolean = false,
    ):Parcelable