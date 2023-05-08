package com.example.cyclistance.feature_settings.presentation.setting_screen

sealed class SettingEvent{
    object ToggleTheme: SettingEvent()
    object SignOut: SettingEvent()
}