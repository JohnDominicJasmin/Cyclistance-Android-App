package com.example.cyclistance.feature_readable_displays.domain.use_case.read_intro_slider

import com.example.cyclistance.feature_readable_displays.domain.repository.IntroSliderRepository
import kotlinx.coroutines.flow.Flow

class UserCompletedWalkThroughUseCase(
    private val repository: IntroSliderRepository) {

    operator fun invoke():Flow<Boolean> =
        repository.userCompletedWalkThrough()


}