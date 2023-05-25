package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.core.utils.validation.InputValidate.containsNumeric
import com.example.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.validation.InputValidate.numberOfCharactersEnough
import com.example.cyclistance.feature_settings.domain.exceptions.SettingExceptions
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository

class UpdateProfileUseCase(private val repository: SettingRepository) {
    suspend operator fun invoke(photoUri: String?, name: String){
        if(name.isEmpty()){
           throw SettingExceptions.NameException()
        }
        if(name.containsNumeric() || name.containsSpecialCharacters()){
            throw SettingExceptions.NameException("Name must not contain Numbers or Special Characters.")
        }
        if(!name.numberOfCharactersEnough()){
            throw SettingExceptions.NameException("Name is too short.")
        }
        repository.updateProfile(photoUri, name)
    }
}