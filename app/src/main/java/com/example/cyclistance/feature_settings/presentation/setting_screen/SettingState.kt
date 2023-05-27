package com.example.cyclistance.feature_settings.presentation.setting_screen

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class SettingState(
    val isDarkTheme: Boolean = false
) : Parcelable
