package com.example.cyclistance.feature_intro_slider.domain.use_case.completed_intro_slider

import com.example.cyclistance.feature_intro_slider.domain.repository.IntroSliderRepository

class CompletedIntroSliderUseCase(
    private val repository: IntroSliderRepository) {

    suspend operator fun invoke() {
        repository.setUserCompletedWalkThrough()
    }
}