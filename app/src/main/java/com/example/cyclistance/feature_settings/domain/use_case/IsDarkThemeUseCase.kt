package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.feature_settings.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow

class IsDarkThemeUseCase(
    private val settingRepository: SettingRepository){

    operator fun invoke(): Flow<Boolean?> {
        return settingRepository.isDarkTheme()
    }

}
