package com.example.cyclistance.feature_readable_displays.presentation.intro_slider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_readable_displays.presentation.domain.use_case.IntroSliderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroSliderViewModel @Inject
constructor(private val introSliderUseCase: IntroSliderUseCase): ViewModel() {


    fun userCompletedWalkThrough() {
        viewModelScope.launch {
            introSliderUseCase.completedIntroSliderUseCase()
        }
    }

}