package com.example.cyclistance.feature_readable_displays.domain.use_case.completed_intro_slider
import com.example.cyclistance.feature_readable_displays.domain.repository.IntroSliderRepository

class CompletedIntroSliderUseCase(
    private val repository: IntroSliderRepository) {

    suspend operator fun invoke(){
        repository.setUserCompletedWalkThrough()
    }
}