package com.example.cyclistance.feature_readable_displays.presentation.intro_slider

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_readable_displays.domain.use_case.IntroSliderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroSliderViewModel @Inject constructor(

    private val savedStateHandle: SavedStateHandle,
    private val introSliderUseCase: IntroSliderUseCase,
    private val authUseCase: AuthenticationUseCase,
    private val defaultDispatcher: CoroutineDispatcher
    ) : ViewModel() {


    fun onEvent(event: IntroSliderEvent) {
        when (event) {
            is IntroSliderEvent.UserCompletedWalkThrough -> {
                viewModelScope.launch(context = defaultDispatcher) {
                    introSliderUseCase.completedIntroSliderUseCase()
                }
            }

        }
    }


}