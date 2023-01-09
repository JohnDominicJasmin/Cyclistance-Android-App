package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import com.example.cyclistance.core.utils.validation.InputValidate.containsNumeric
import com.example.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.validation.InputValidate.numberOfCharactersEnough
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions

class UpdateProfileUseCase(private val repository: AuthRepository<*>) {
    suspend operator fun invoke(photoUri: String?, name: String){
        if(name.isEmpty()){
           throw MappingExceptions.NameException()
        }
        if(name.containsNumeric() || name.containsSpecialCharacters()){
            throw MappingExceptions.NameException("Name must not contain Numbers or Special Characters.")
        }
        if(!name.numberOfCharactersEnough()){
            throw MappingExceptions.NameException("Name is too short.")
        }
        repository.updateProfile(photoUri, name)
    }
}