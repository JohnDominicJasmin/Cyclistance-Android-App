package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.validation.InputValidate.containsNumeric
import com.example.cyclistance.core.utils.validation.InputValidate.numberOfCharactersEnough
import com.google.firebase.auth.AuthCredential

class UpdateProfileUseCase(private val repository: AuthRepository<AuthCredential>) {
    suspend operator fun invoke(photoUri: Uri?, name: String){
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