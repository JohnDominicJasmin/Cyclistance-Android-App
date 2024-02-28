package com.myapp.cyclistance.feature_mapping.domain.use_case.user

import com.myapp.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class ConfirmDetailsUseCase (private val repository: MappingRepository) {
    suspend operator fun invoke(user: UserItem) {
        val bikeType = user.getBikeType()
        val description = user.getDescription()

        if (bikeType.isNullOrEmpty()) {
            throw MappingExceptions.BikeTypeException()
        }
        if (description.isNullOrEmpty()) {
            throw MappingExceptions.DescriptionException()
        }
        repository.createUser(user)
    }
}