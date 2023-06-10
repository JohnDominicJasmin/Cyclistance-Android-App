package com.example.cyclistance.feature_intro_slider.domain.use_case.read_intro_slider

import com.example.cyclistance.feature_intro_slider.domain.repository.IntroSliderRepository
import kotlinx.coroutines.flow.Flow

class UserCompletedWalkThroughUseCase(
    private val repository: IntroSliderRepository) {

    operator fun invoke():Flow<Boolean> =
        repository.userCompletedWalkThrough()


}