package com.example.cyclistance.feature_readable_displays.presentation.splash_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_readable_displays.domain.use_case.IntroSliderUseCase
import com.example.cyclistance.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val introSliderUseCase: IntroSliderUseCase,
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _splashScreenState: MutableState<SplashScreenState> = mutableStateOf(SplashScreenState())
    val splashScreenState by _splashScreenState


    init {
        isUserCompletedWalkThrough()
    }


    private fun isUserCompletedWalkThrough() {

        viewModelScope.launch {
            runCatching {

                introSliderUseCase.readIntroSliderUseCase().collect { userCompletedWalkThrough ->
                    if (userCompletedWalkThrough) {

                        if (isUserSignedIn()) {
                            _splashScreenState.value =
                                splashScreenState.copy(navigationStartingDestination = Screens.MappingScreen.route)
                            return@collect
                        }
                        _splashScreenState.value =
                            splashScreenState.copy(navigationStartingDestination = Screens.SignInScreen.route)

                    } else {
                        _splashScreenState.value =
                            splashScreenState.copy(navigationStartingDestination = Screens.IntroSliderScreen.route)
                    }
                }

            }.onFailure {
                Timber.e("IntroSlider DataStore Reading Failed: ${it.localizedMessage}")
            }
        }

    }
    private  fun isUserSignedIn():Boolean{
        return (authUseCase.isSignedInWithProviderUseCase() == true || authUseCase.isEmailVerifiedUseCase() == true) &&
                authUseCase.hasAccountSignedInUseCase()
    }
}
