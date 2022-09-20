package com.example.cyclistance.feature_settings.presentation.setting_main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_settings.domain.use_case.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingUseCase: SettingUseCase) : ViewModel() {

    val isDarkTheme = settingUseCase.isDarkThemeUseCase().asLiveData()


    fun onEvent(event: SettingEvent) {
        when (event) {
            is SettingEvent.ToggleTheme -> {
                toggleTheme()
            }
        }

    }

    private fun toggleTheme() {
        viewModelScope.launch {
            kotlin.runCatching {
                settingUseCase.toggleThemeUseCase()
            }.onFailure { exception ->
                Timber.e(exception.message)
            }
        }
    }

}