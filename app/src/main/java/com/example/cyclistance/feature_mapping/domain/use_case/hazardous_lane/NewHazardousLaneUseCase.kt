package com.example.cyclistance.feature_mapping.domain.use_case.hazardous_lane

import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class NewHazardousLaneUseCase(private val repository: MappingRepository) {


    suspend operator fun invoke(
        onAddedHazardousMarker: (HazardousLaneMarker) -> Unit,
        onModifiedHazardousMarker: (HazardousLaneMarker) -> Unit,
        onRemovedHazardousMarker: (markerId: String) -> Unit,

        ) {

        repository.addHazardousLaneListener(
            onAddedHazardousMarker = onAddedHazardousMarker,
            onModifiedHazardousMarker = onModifiedHazardousMarker,
            onRemovedHazardousMarker = onRemovedHazardousMarker)
    }

    suspend operator fun invoke(hazardousLaneMarker: HazardousLaneMarker) {
        repository.addNewHazardousLane(hazardousLaneMarker)
    }

}