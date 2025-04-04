package com.myapp.cyclistance.feature_user_profile.domain.use_case

import com.myapp.cyclistance.core.utils.validation.InputValidate.containsNumeric
import com.myapp.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.myapp.cyclistance.core.utils.validation.InputValidate.numberOfCharactersEnough
import com.myapp.cyclistance.feature_user_profile.domain.exceptions.UserProfileExceptions
import com.myapp.cyclistance.feature_user_profile.domain.repository.UserProfileRepository

class UpdateAuthProfileUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(photoUri: String?, name: String){
        if(name.isEmpty()){
           throw UserProfileExceptions.NameException()
        }
        if(name.containsNumeric() || name.containsSpecialCharacters()){
            throw UserProfileExceptions.NameException("Name must not contain Numbers or Special Characters.")
        }
        if(!name.numberOfCharactersEnough()){
            throw UserProfileExceptions.NameException("Name is too short.")
        }
        repository.updateProfile(photoUri, name)
    }
}