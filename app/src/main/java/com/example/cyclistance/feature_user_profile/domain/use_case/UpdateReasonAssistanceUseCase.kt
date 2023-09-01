package com.example.cyclistance.feature_user_profile.domain.use_case

import com.example.cyclistance.feature_user_profile.domain.model.ReasonAssistanceModel
import com.example.cyclistance.feature_user_profile.domain.repository.UserProfileRepository

class UpdateReasonAssistanceUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(
        id: String,
        reasonAssistanceModel: ReasonAssistanceModel) {
        repository.updateReasonAssistance(id = id, reasonAssistanceModel = reasonAssistanceModel)
    }
}