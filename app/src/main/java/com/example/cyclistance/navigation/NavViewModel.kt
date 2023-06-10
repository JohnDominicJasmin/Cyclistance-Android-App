package com.example.cyclistance.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.NavConstants.NAV_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_intro_slider.domain.use_case.IntroSliderUseCase
import com.example.cyclistance.navigation.state.NavState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val introSliderUseCase: IntroSliderUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val authUseCase: AuthenticationUseCase
):ViewModel() {


    private val _state = MutableStateFlow(savedStateHandle[NAV_VM_STATE_KEY] ?: NavState())
    val state = _state.asStateFlow()

    init {
        getStartingDestination()
    }

    private fun getStartingDestination() {

        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {

                introSliderUseCase.readIntroSliderUseCase().collect { userCompletedWalkThrough ->

                    if (!userCompletedWalkThrough) {
                        _state.update { it.copy(navigationStartingDestination = Screens.IntroSliderScreen.route) }
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
            savedStateHandle[NAV_VM_STATE_KEY] = state.value
        }

    }


    private fun isUserSignedIn(): Boolean {
        return (authUseCase.isSignedInWithProviderUseCase() == true || authUseCase.isEmailVerifiedUseCase() == true) &&
               authUseCase.hasAccountSignedInUseCase()
    }


}