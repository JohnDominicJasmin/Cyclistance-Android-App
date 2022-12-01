package com.example.cyclistance.feature_mapping_screen.domain.use_case.user

import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class ConfirmDetailsUseCase (private val repository: MappingRepository) {
    suspend operator fun invoke(user: UserItem) {
        val confirmation = user.userAssistance?.confirmationDetail

        if (confirmation?.bikeType.isNullOrEmpty()) {
            throw MappingExceptions.BikeTypeException()
        }
        if (confirmation?.description.isNullOrEmpty()) {
            throw MappingExceptions.DescriptionException()
        }
        repository.createUser(user)
    }
}