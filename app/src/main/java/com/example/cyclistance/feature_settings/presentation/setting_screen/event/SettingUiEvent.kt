package com.example.cyclistance.feature_settings.presentation.setting_screen.event

sealed class SettingUiEvent {
    object SignOutSuccess: SettingUiEvent()
    object SignOutFailed: SettingUiEvent()
}