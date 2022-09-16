package com.example.cyclistance.feature_readable_displays.domain.repository

import kotlinx.coroutines.flow.Flow

interface IntroSliderRepository {
    fun userCompletedWalkThrough(): Flow<Boolean>
    suspend fun setUserCompletedWalkThrough()
}