package com.example.cyclistance.feature_mapping.domain.use_case.websockets.hazardous_lane

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class RemoveHazardousListenerUseCase(private val repository: MappingRepository) {
    operator fun invoke() = repository.removeHazardousLaneListener()
}