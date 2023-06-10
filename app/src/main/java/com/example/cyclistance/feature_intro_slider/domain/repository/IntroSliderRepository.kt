package com.example.cyclistance.feature_intro_slider.domain.repository

import kotlinx.coroutines.flow.Flow

interface IntroSliderRepository {
    fun userCompletedWalkThrough(): Flow<Boolean>
    suspend fun setUserCompletedWalkThrough()
}