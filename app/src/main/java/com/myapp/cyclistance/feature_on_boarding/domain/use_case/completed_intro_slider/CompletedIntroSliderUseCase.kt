package com.myapp.cyclistance.feature_on_boarding.domain.use_case.completed_intro_slider

import com.myapp.cyclistance.feature_on_boarding.domain.repository.IntroSliderRepository

class CompletedIntroSliderUseCase(
    private val repository: IntroSliderRepository) {

    suspend operator fun invoke() {
        repository.setUserCompletedWalkThrough()
    }
}