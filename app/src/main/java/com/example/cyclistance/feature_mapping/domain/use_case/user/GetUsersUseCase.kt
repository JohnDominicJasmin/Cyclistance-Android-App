package com.example.cyclistance.feature_mapping.domain.use_case.user

import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUsersUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Flow<NearbyCyclist> =
        repository.getUsers(latitude = latitude, longitude = longitude)

}