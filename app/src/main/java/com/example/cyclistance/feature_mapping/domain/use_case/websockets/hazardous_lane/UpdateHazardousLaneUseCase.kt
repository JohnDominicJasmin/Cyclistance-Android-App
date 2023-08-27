package com.example.cyclistance.feature_mapping.domain.use_case.websockets.hazardous_lane

import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class UpdateHazardousLaneUseCase(private val repository: MappingRepository) {

    suspend operator fun invoke(hazardousLaneMarker: HazardousLaneMarker) {
        repository.updateHazardousLane(
            label = hazardousLaneMarker.label,
            description = hazardousLaneMarker.description,
            id = hazardousLaneMarker.id
        )
    }

}