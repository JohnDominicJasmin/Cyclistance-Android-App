package com.example.cyclistance.feature_settings.presentation.setting_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.SettingConstants.SETTING_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_settings.domain.use_case.SettingUseCase
import com.example.cyclistance.feature_settings.presentation.setting_screen.event.SettingUiEvent
import com.example.cyclistance.feature_settings.presentation.setting_screen.state.SettingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val settingUseCase: SettingUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val authUseCase: AuthenticationUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle[SETTING_VM_STATE_KEY] ?: SettingState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<SettingUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadTheme()
    }

    private fun signOutAccount() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                authUseCase.signOutUseCase()
            }.onSuccess {
                _eventFlow.emit(value = SettingUiEvent.SignOutSuccess)
            }.onFailure {
                _eventFlow.emit(value = SettingUiEvent.SignOutFailed)
            }
        }.invokeOnCompletion {
            savedStateHandle[MappingConstants.MAPPING_VM_STATE_KEY] = state.value
        }
    }


    private fun loadTheme() {

        viewModelScope.launch(context = defaultDispatcher) {

            settingUseCase.isDarkThemeUseCase().catch {
                Timber.e("Dark Theme DataStore Reading Failed: ${it.localizedMessage}")

            }.onEach { isDarkTheme ->
                _state.update { it.copy(isDarkTheme = isDarkTheme) }
                savedStateHandle[SETTING_VM_STATE_KEY] = state.value
            }.launchIn(this@launch)
        }
    }


    private fun toggleTheme() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                settingUseCase.toggleThemeUseCase()
            }.onFailure { exception ->
                Timber.e(exception.message)
            }
        }
    }


    fun onEvent(event: SettingEvent) {
        when (event) {
            is SettingEvent.ToggleTheme -> {
                toggleTheme()
            }

            is SettingEvent.SignOut -> {
                signOutAccount()
            }
        }

    }
}