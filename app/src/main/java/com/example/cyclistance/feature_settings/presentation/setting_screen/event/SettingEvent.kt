package com.example.cyclistance.feature_settings.presentation.setting_screen.event

sealed class SettingEvent {
    object SignOutSuccess: SettingEvent()
    object SignOutFailed: SettingEvent()
}