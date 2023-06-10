package com.example.cyclistance.feature_intro_slider.domain.use_case

import com.example.cyclistance.feature_intro_slider.domain.use_case.completed_intro_slider.CompletedIntroSliderUseCase
import com.example.cyclistance.feature_intro_slider.domain.use_case.read_intro_slider.UserCompletedWalkThroughUseCase

data class IntroSliderUseCase(
    val readIntroSliderUseCase: UserCompletedWalkThroughUseCase,
    val completedIntroSliderUseCase: CompletedIntroSliderUseCase
)
