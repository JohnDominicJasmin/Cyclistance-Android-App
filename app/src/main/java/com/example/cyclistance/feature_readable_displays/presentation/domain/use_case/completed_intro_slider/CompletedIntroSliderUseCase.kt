package com.example.cyclistance.feature_readable_displays.presentation.domain.use_case.completed_intro_slider
import com.example.cyclistance.feature_readable_displays.presentation.domain.repository.IntroSliderRepository

class CompletedIntroSliderUseCase(
    private val repositoryImpl: IntroSliderRepository) {

    suspend operator fun invoke(){
        repositoryImpl.completedIntroSlider()
    }
}