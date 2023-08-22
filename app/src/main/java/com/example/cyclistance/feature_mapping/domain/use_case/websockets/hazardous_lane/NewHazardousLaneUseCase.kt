package com.example.cyclistance.feature_mapping.domain.use_case.websockets.hazardous_lane

import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLane
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class NewHazardousLaneUseCase(private val repository: MappingRepository) {


    suspend operator fun invoke(onNewHazardousLane: (HazardousLane) -> Unit) =
        repository.addHazardousLaneListener(onNewHazardousLane = onNewHazardousLane)

    suspend operator fun invoke(hazardousLaneMarker: HazardousLaneMarker) =
        repository.addNewHazardousLane(hazardousLaneMarker)

}