package com.example.cyclistance.feature_readable_displays.presentation.domain.use_case.read_intro_slider

import com.example.cyclistance.feature_readable_displays.presentation.domain.repository.IntroSliderRepository
import kotlinx.coroutines.flow.Flow

class ReadIntroSliderStateUseCase(
    private val repositoryImpl: IntroSliderRepository) {

    operator fun invoke():Flow<Boolean> =
        repositoryImpl.readIntroSliderState()


}