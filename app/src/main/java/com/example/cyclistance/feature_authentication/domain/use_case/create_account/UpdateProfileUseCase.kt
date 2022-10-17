package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.core.utils.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.InputValidate.containsNumeric
import com.example.cyclistance.core.utils.InputValidate.numberOfCharactersEnough
import com.google.firebase.auth.AuthCredential

class UpdateProfileUseCase(private val repository: AuthRepository<AuthCredential>) {
    suspend operator fun invoke(photoUri: Uri?, name: String){
        if(name.isEmpty()){
           throw MappingExceptions.NameException()
        }
        if(containsNumeric(name) || containsSpecialCharacters(name)){
            throw MappingExceptions.NameException("Name must not contain Numbers or Special Characters.")
        }
        if(!numberOfCharactersEnough(name)){
            throw MappingExceptions.NameException("Name is too short.")
        }
        repository.updateProfile(photoUri, name)
    }
}