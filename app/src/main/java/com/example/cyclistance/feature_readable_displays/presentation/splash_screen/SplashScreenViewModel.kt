package com.example.cyclistance.feature_readable_displays.presentation.splash_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_readable_displays.domain.use_case.IntroSliderUseCase
import com.example.cyclistance.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val introSliderUseCase: IntroSliderUseCase) : ViewModel() {

    private val state: MutableState<SplashScreenState> = mutableStateOf(SplashScreenState())
    val _state: State<SplashScreenState> = state


    init {
        viewModelScope.launch {

            kotlin.runCatching {
                introSliderUseCase.readIntroSliderUseCase()

            }.onSuccess { result ->
                result.collect { userCompletedWalkThrough ->
                    if (userCompletedWalkThrough) {
                        state.value =
                            SplashScreenState(navigationStartingDestination = Screens.SignInScreen.route)
                    } else {
                        state.value =
                            SplashScreenState(navigationStartingDestination = Screens.SplashScreen.route)
                    }
                }
            }.onFailure {
                Timber.e("IntroSlider DataStore Reading Failed: ${it.localizedMessage}")
            }


        }
    }


}