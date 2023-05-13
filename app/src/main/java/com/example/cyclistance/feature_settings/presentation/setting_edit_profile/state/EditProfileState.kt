package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditProfileState(
    val nameSnapshot: String = "",
    val phoneNumberSnapshot: String = "",
    val isLoading: Boolean = false,
    ):Parcelable