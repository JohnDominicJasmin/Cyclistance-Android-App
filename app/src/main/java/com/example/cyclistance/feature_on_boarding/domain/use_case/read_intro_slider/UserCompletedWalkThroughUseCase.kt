package com.example.cyclistance.feature_on_boarding.domain.use_case.read_intro_slider

import com.example.cyclistance.feature_on_boarding.domain.repository.IntroSliderRepository
import kotlinx.coroutines.flow.Flow

class UserCompletedWalkThroughUseCase(
    private val repository: IntroSliderRepository) {

    operator fun invoke():Flow<Boolean> =
        repository.userCompletedWalkThrough()


}