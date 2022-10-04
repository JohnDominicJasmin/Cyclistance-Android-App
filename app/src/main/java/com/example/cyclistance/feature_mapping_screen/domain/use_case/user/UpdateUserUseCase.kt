package com.example.cyclistance.feature_mapping_screen.domain.use_case.user

import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class UpdateUserUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(itemId: String, user: User){

        with(user.userAssistance.confirmationDetails) {

            if (bikeType.isEmpty()){
                throw MappingExceptions.BikeTypeException()
            }
            if(description.isEmpty()){
                throw MappingExceptions.DescriptionException()
            }

             repository.updateUser(itemId, user)

        }

    }
}