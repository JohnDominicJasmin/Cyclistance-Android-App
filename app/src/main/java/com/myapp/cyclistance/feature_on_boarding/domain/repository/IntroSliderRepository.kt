package com.myapp.cyclistance.feature_on_boarding.domain.repository

import kotlinx.coroutines.flow.Flow

interface IntroSliderRepository {
    fun userCompletedWalkThrough(): Flow<Boolean>
    suspend fun setUserCompletedWalkThrough()
}