package com.myapp.cyclistance.feature_mapping.domain.use_case.hazardous_lane

import com.myapp.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarkerDetails
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class NewHazardousLaneUseCase(private val repository: MappingRepository) {


    suspend operator fun invoke(
        onAddedHazardousMarker: (HazardousLaneMarkerDetails) -> Unit,
        onModifiedHazardousMarker: (HazardousLaneMarkerDetails) -> Unit,
        onRemovedHazardousMarker: (markerId: String) -> Unit,

        ) {

        repository.addHazardousLaneListener(
            onAddedHazardousMarker = onAddedHazardousMarker,
            onModifiedHazardousMarker = onModifiedHazardousMarker,
            onRemovedHazardousMarker = onRemovedHazardousMarker)
    }

    suspend operator fun invoke(hazardousLaneMarker: HazardousLaneMarkerDetails) {

        hazardousLaneMarker.latitude ?: throw MappingExceptions.LocationException(message = "Searching GPS")
        hazardousLaneMarker.longitude ?: throw MappingExceptions.LocationException(message = "Searching GPS")

        val fullAddress = repository.getFullAddress(
            latitude = hazardousLaneMarker.latitude,
            longitude = hazardousLaneMarker.longitude
        )

        repository.addNewHazardousLane(hazardousLaneMarker.copy(address = fullAddress))
    }

}