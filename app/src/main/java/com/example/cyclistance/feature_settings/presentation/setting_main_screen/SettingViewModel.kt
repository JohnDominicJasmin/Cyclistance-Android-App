package com.example.cyclistance.feature_settings.presentation.setting_main_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.SettingConstants.SETTING_VM_STATE_KEY
import com.example.cyclistance.feature_settings.domain.use_case.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val settingUseCase: SettingUseCase) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle[SETTING_VM_STATE_KEY] ?: SettingState())
    val state = _state.asStateFlow()


    init{
        loadTheme()
    }

    private fun loadTheme(){

        viewModelScope.launch {
            runCatching {
                settingUseCase.isDarkThemeUseCase().collect { isDarkTheme ->
                    _state.update { it.copy(isDarkTheme = isDarkTheme) }
                    savedStateHandle[SETTING_VM_STATE_KEY] = state.value
                }
            }.onFailure {
                Timber.e("Dark Theme DataStore Reading Failed: ${it.localizedMessage}")

            }
        }
    }

    fun onEvent(event: SettingEvent) {
        when (event) {
            is SettingEvent.ToggleTheme -> {
                toggleTheme()
            }
        }

    }

    private fun toggleTheme() {
        viewModelScope.launch {
            runCatching {
                settingUseCase.toggleThemeUseCase()
            }.onFailure { exception ->
                Timber.e(exception.message)
            }
        }
    }

}