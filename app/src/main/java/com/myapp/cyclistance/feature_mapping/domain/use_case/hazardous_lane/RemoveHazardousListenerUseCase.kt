package com.myapp.cyclistance.feature_mapping.domain.use_case.hazardous_lane

import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class RemoveHazardousListenerUseCase(private val repository: MappingRepository) {
    operator fun invoke() = repository.removeHazardousLaneListener()
}