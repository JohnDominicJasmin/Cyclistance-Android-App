package com.example.cyclistance.feature_readable_displays.presentation.intro_slider

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.ReadableConstants
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_readable_displays.domain.use_case.IntroSliderUseCase
import com.example.cyclistance.navigation.MainScreenState
import com.example.cyclistance.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IntroSliderViewModel @Inject
constructor(

    private val savedStateHandle: SavedStateHandle,
    private val introSliderUseCase: IntroSliderUseCase,
    private val authUseCase: AuthenticationUseCase) : ViewModel() {


    private val _state: MutableStateFlow<MainScreenState> = MutableStateFlow(
        savedStateHandle[ReadableConstants.MAIN_SCREEN_VM_STATE_KEY] ?: MainScreenState())

    val state = _state.asStateFlow()

    init {
        getStartingDestination()
    }

    private fun getStartingDestination() {

        viewModelScope.launch {
            runCatching {

                introSliderUseCase.readIntroSliderUseCase().collect { userCompletedWalkThrough ->

                    if (!userCompletedWalkThrough) {
                        return@collect
                    }

                    if (isUserSignedIn()) {
                         _state.update { it.copy(navigationStartingDestination = Screens.MappingScreen.route) }
                        return@collect
                    }

                    _state.update { it.copy(navigationStartingDestination = Screens.SignInScreen.route) }
                }

            }.onFailure {
                Timber.e("IntroSlider DataStore Reading Failed: ${it.localizedMessage}")
            }
        }.invokeOnCompletion {
            savedStateHandle[ReadableConstants.MAIN_SCREEN_VM_STATE_KEY] = state.value
        }

    }

    private fun isUserSignedIn(): Boolean {
        return (authUseCase.isSignedInWithProviderUseCase() == true || authUseCase.isEmailVerifiedUseCase() == true) &&
               authUseCase.hasAccountSignedInUseCase()
    }


    fun onEvent(event: IntroSliderEvent) {
        when (event) {
            is IntroSliderEvent.UserCompletedWalkThrough -> {
                viewModelScope.launch {
                    introSliderUseCase.completedIntroSliderUseCase()
                }
            }

        }
    }


}