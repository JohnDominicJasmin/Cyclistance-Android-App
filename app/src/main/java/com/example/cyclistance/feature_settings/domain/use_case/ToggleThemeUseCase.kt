package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.feature_settings.domain.repository.SettingRepository
import kotlinx.coroutines.flow.first

class ToggleThemeUseCase(
    private val settingRepository: SettingRepository) {

    suspend operator fun invoke(){
        settingRepository.toggleTheme(value = !settingRepository.isDarkTheme().first()!!)
    }
}