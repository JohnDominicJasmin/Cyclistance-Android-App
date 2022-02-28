package com.example.cyclistance.feature_readable_displays.presentation.domain.repository

import kotlinx.coroutines.flow.Flow

interface IntroSliderRepository {

    fun readIntroSliderState(): Flow<Boolean>
    suspend fun completedIntroSlider()
}