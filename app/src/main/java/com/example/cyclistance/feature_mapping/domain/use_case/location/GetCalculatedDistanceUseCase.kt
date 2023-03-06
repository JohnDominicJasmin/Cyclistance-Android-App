package com.example.cyclistance.feature_mapping.domain.use_case.location

import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class GetCalculatedDistanceUseCase(private val repository: MappingRepository){

    operator fun invoke(
        startingLocation: Location, destinationLocation: Location
    ):Double{
        return repository.getCalculateDistance(
            startingLocation = startingLocation,
            destinationLocation = destinationLocation
        )

    }
}