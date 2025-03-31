package com.myapp.cyclistance.feature_mapping.domain.use_case.hazardous_lane

import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class UpdateHazardousLaneUseCase(private val repository: MappingRepository) {

    suspend operator fun invoke(hazardousLaneMarker: HazardousLaneMarkerDetails) {
        repository.updateHazardousLane(
            label = hazardousLaneMarker.label,
            description = hazardousLaneMarker.description,
            id = hazardousLaneMarker.id
        )
    }

}