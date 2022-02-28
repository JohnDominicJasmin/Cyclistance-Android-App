package com.example.cyclistance.feature_readable_displays.presentation.domain.use_case

import com.example.cyclistance.feature_readable_displays.presentation.domain.use_case.completed_intro_slider.CompletedIntroSliderUseCase
import com.example.cyclistance.feature_readable_displays.presentation.domain.use_case.read_intro_slider.ReadIntroSliderStateUseCase

data class IntroSliderUseCase(
    val readIntroSliderUseCase: ReadIntroSliderStateUseCase,
    val completedIntroSliderUseCase: CompletedIntroSliderUseCase
)
