package com.example.cyclistance.feature_settings.presentation.setting_main_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_settings.domain.use_case.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingUseCase: SettingUseCase): ViewModel() {




    fun toggleTheme() {
        viewModelScope.launch {
            settingUseCase.toggleThemeUseCase()
        }
    }

    fun isDarkTheme(): LiveData<Boolean> = settingUseCase.isDarkThemeUseCase().asLiveData()


}