package com.myapp.cyclistance.feature_user_profile.domain.use_case

import com.myapp.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.myapp.cyclistance.feature_user_profile.domain.repository.UserProfileRepository

class GetNameUseCase(private val repository: UserProfileRepository) {

     suspend operator fun invoke(): String {

        return repository.getName().takeIf { !it.isNullOrEmpty() } ?: throw MappingExceptions.NameException(message = "Name not found")

    }
}