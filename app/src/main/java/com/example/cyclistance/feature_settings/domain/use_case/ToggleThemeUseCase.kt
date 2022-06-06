package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.feature_settings.domain.repository.SettingRepository

class ToggleThemeUseCase(
    private val settingRepository: SettingRepository) {

    suspend operator fun invoke(){
        settingRepository.toggleTheme()
    }
}