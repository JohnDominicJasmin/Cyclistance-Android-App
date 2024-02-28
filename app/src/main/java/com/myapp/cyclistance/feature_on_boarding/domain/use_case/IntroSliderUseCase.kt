package com.myapp.cyclistance.feature_on_boarding.domain.use_case

import com.myapp.cyclistance.feature_on_boarding.domain.use_case.completed_intro_slider.CompletedIntroSliderUseCase
import com.myapp.cyclistance.feature_on_boarding.domain.use_case.read_intro_slider.UserCompletedWalkThroughUseCase

data class IntroSliderUseCase(
    val readIntroSliderUseCase: UserCompletedWalkThroughUseCase,
    val completedIntroSliderUseCase: CompletedIntroSliderUseCase
)
