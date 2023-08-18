package com.example.cyclistance.feature_mapping.domain.use_case.websockets.hazardous_lane

import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository

class NewHazardousLaneUseCase(private val repository: MappingSocketRepository) {
    suspend operator fun invoke() = repository.getNewHazardousLaneUpdates()
    suspend operator fun invoke(hazardousLaneMarker: HazardousLaneMarker) =
        repository.addNewHazardousLane(hazardousLaneMarker)

}