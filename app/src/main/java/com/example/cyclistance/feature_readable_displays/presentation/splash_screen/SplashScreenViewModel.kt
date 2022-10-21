package com.example.cyclistance.feature_readable_displays.presentation.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_readable_displays.domain.use_case.IntroSliderUseCase
import com.example.cyclistance.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val introSliderUseCase: IntroSliderUseCase,
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _state: MutableStateFlow<SplashScreenState> = MutableStateFlow(SplashScreenState())
    val state = _state.asStateFlow()


    init {
        isUserCompletedWalkThrough()
    }


    private fun isUserCompletedWalkThrough() {

        viewModelScope.launch {
            runCatching {

                introSliderUseCase.readIntroSliderUseCase().collect { userCompletedWalkThrough ->
                    if (userCompletedWalkThrough) {

                        if (isUserSignedIn()) {

                            _state.update {
                                it.copy(navigationStartingDestination = Screens.MappingScreen.route)
                            }

                            return@collect
                        }
                        _state.update {
                            it.copy(navigationStartingDestination = Screens.SignInScreen.route)
                        }

                    } else {
                        _state.update {
                            it.copy(navigationStartingDestination = Screens.IntroSliderScreen.route)
                        }
                    }
                }

            }.onFailure {
                Timber.e("IntroSlider DataStore Reading Failed: ${it.localizedMessage}")
            }
        }

    }
    private fun isUserSignedIn():Boolean{
        return (authUseCase.isSignedInWithProviderUseCase() == true || authUseCase.isEmailVerifiedUseCase() == true) &&
                authUseCase.hasAccountSignedInUseCase()
    }
}
