package com.myapp.cyclistance.feature_settings.presentation.setting_screen.event

sealed class SettingVmEvent{
    object SignOut: SettingVmEvent()
    object ToggleTheme: SettingVmEvent()

}
