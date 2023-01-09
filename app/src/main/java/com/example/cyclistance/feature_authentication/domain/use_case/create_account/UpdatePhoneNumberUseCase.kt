package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import android.net.Uri
import com.example.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.validation.InputValidate.isDigit
import com.example.cyclistance.core.utils.validation.InputValidate.isPhoneNumberLongEnough
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.google.firebase.auth.AuthCredential

class UpdatePhoneNumberUseCase(private val repository: AuthRepository<AuthCredential, Uri>) {
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

        if(!userPhoneNumber.startsWith(prefix = "9")){
            throw MappingExceptions.PhoneNumberException("Phone number must start with 9")
        }

        if(!userPhoneNumber.isPhoneNumberLongEnough()){
            throw MappingExceptions.PhoneNumberException("Phone number is invalid")
        }



        repository.updatePhoneNumber(phoneNumber)
    }
}
