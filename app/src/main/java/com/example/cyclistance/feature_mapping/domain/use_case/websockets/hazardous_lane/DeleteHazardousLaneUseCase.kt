package com.example.cyclistance.feature_mapping.domain.use_case.websockets.hazardous_lane

import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository

class DeleteHazardousLaneUseCase(private val repository: MappingSocketRepository){
    suspend operator fun invoke(id: String) = repository.deleteHazardousLane(id)
}
