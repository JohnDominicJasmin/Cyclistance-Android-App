package com.example.cyclistance.feature_readable_displays.presentation.splash_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import com.example.cyclistance.feature_readable_displays.domain.use_case.IntroSliderUseCase
import com.example.cyclistance.navigation.Screens
import com.google.firebase.auth.FacebookAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val introSliderUseCase: IntroSliderUseCase,
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _splashScreenState: MutableState<SplashScreenState> =
        mutableStateOf(SplashScreenState())
    val splashScreenState: State<SplashScreenState> = this._splashScreenState


    init {
        isUserCompletedWalkThrough()
    }


    private fun isUserCompletedWalkThrough() {
        viewModelScope.launch {
            kotlin.runCatching {

                introSliderUseCase.readIntroSliderUseCase()

            }.onSuccess { result ->
                result.collect { userCompletedWalkThrough ->
                    if (userCompletedWalkThrough) {

                        if (authUseCase.isSignedInWithProviderUseCase() == true || authUseCase.isEmailVerifiedUseCase() == true) {
                            _splashScreenState.value = SplashScreenState(navigationStartingDestination = Screens.MappingScreen.route)
                            return@collect
                        }
                        _splashScreenState.value = SplashScreenState(navigationStartingDestination = Screens.SignInScreen.route)

                    } else {
                        _splashScreenState.value = SplashScreenState(navigationStartingDestination = Screens.IntroSliderScreen.route)
                    }
                }
            }.onFailure {
                Timber.e("IntroSlider DataStore Reading Failed: ${it.localizedMessage}")
            }

        }
    }
}