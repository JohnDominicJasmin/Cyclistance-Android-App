package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import com.example.cyclistance.core.utils.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.InputValidate.isDigit
import com.example.cyclistance.core.utils.InputValidate.isPhoneNumberLongEnough
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.google.firebase.auth.AuthCredential

class UpdatePhoneNumberUseCase(private val repository: AuthRepository<AuthCredential>) {
    suspend operator fun invoke(phoneNumber:String){

        val userPhoneNumber = phoneNumber.trim()

        if(userPhoneNumber.isEmpty()){
            throw MappingExceptions.PhoneNumberException()
        }

        if(!userPhoneNumber.isDigit()){
            throw MappingExceptions.PhoneNumberException("Phone number must contain only numbers.")
        }

        if(userPhoneNumber.containsSpecialCharacters()){
            throw MappingExceptions.PhoneNumberException("Phone number must not contain special characters.")
        }

        if(!userPhoneNumber.isPhoneNumberLongEnough()){
            throw MappingExceptions.PhoneNumberException("Phone number is invalid")
        }

        if(!userPhoneNumber.startsWith(prefix = "9")){
            throw MappingExceptions.PhoneNumberException("Phone number must start with 9")
        }

        repository.updatePhoneNumber(phoneNumber)
    }
}
