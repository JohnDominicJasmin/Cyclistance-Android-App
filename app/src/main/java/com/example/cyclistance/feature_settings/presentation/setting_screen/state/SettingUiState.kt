package com.example.cyclistance.feature_settings.presentation.setting_screen.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class SettingUiState(
    val urlToOpen: String? = null,
) : Parcelable
