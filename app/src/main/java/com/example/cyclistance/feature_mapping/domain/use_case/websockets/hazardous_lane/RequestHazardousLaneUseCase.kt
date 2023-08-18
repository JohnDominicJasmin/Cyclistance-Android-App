package com.example.cyclistance.feature_mapping.domain.use_case.websockets.hazardous_lane

import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository

class RequestHazardousLaneUseCase(private val repository: MappingSocketRepository) {
    suspend operator fun invoke() = repository.requestHazardousLane()
}