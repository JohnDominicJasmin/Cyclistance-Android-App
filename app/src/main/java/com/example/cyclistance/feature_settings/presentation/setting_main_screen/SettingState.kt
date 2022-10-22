package com.example.cyclistance.feature_settings.presentation.setting_main_screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SettingState(
    val isDarkTheme: Boolean = false
) : Parcelable
