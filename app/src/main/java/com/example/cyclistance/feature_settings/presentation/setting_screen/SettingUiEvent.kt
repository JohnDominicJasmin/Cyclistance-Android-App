package com.example.cyclistance.feature_settings.presentation.setting_screen

sealed class SettingUiEvent {
    object SignOutSuccess: SettingUiEvent()
    object SignOutFailed: SettingUiEvent()
}