package com.example.cyclistance.feature_mapping.domain.use_case.intent_action

import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository

class IntentActionUseCase(private val repository: MappingUiStoreRepository) {
    suspend operator fun invoke() = repository.getMappingActionIntent()
    suspend operator fun invoke(actionIntent: String) {
        repository.setMappingActionIntent(actionIntent)
    }
}