package com.myapp.cyclistance.feature_settings.presentation.setting_screen.event

sealed class SettingUiEvent{
    object ClickToggleTheme: SettingUiEvent()
    object ClickResetPassword: SettingUiEvent()
    object ClickEditProfile: SettingUiEvent()
    object DismissWebView: SettingUiEvent()
    data class OpenWebView(val url: String): SettingUiEvent()

}
