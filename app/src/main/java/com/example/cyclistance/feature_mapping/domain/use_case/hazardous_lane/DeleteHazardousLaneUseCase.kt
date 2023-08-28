package com.example.cyclistance.feature_mapping.domain.use_case.hazardous_lane

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class DeleteHazardousLaneUseCase(private val repository: MappingRepository){
    suspend operator fun invoke(id: String) = repository.deleteHazardousLane(id)
}
