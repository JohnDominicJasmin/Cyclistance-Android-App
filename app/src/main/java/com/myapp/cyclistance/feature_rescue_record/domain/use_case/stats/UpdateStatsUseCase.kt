package com.myapp.cyclistance.feature_rescue_record.domain.use_case.stats

import com.myapp.cyclistance.feature_rescue_record.domain.repository.RescueRecordRepository
import com.myapp.cyclistance.feature_user_profile.domain.model.UserStats

class UpdateStatsUseCase(private val repository: RescueRecordRepository) {
    suspend operator fun invoke(userStats: UserStats) {
        repository.updateStats(userStats = userStats)
    }
}