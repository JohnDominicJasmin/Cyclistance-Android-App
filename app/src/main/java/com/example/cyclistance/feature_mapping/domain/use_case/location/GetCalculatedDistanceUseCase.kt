package com.example.cyclistance.feature_mapping.domain.use_case.location

import com.example.cyclistance.feature_mapping.domain.model.api.user.LocationModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class GetCalculatedDistanceUseCase(private val repository: MappingRepository){

    operator fun invoke(
        startingLocation: LocationModel, destinationLocation: LocationModel
    ):Double{
        return repository.getCalculateDistance(
            startingLocation = startingLocation,
            destinationLocation = destinationLocation
        )

    }
}