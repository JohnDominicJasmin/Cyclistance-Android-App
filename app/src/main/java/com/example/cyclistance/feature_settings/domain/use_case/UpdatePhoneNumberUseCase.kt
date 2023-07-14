package com.example.cyclistance.feature_settings.domain.use_case

import com.example.cyclistance.core.utils.validation.InputValidate.containsSpecialCharacters
import com.example.cyclistance.core.utils.validation.InputValidate.isDigit
import com.example.cyclistance.core.utils.validation.InputValidate.isPhoneNumberEnough
import com.example.cyclistance.feature_settings.domain.exceptions.SettingExceptions
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository

class UpdatePhoneNumberUseCase(private val repository: SettingRepository) {
    suspend operator fun invoke(phoneNumber:String){

        val userPhoneNumber = phoneNumber.trim()

        if(userPhoneNumber.isEmpty()){
            throw SettingExceptions.PhoneNumberException()
        }

        if(!userPhoneNumber.isDigit()){
            throw SettingExceptions.PhoneNumberException("Phone number must contain only numbers.")
        }

        if(userPhoneNumber.containsSpecialCharacters()){
            throw SettingExceptions.PhoneNumberException("Phone number must not contain special characters.")
        }

        if(!userPhoneNumber.startsWith(prefix = "9")){
            throw SettingExceptions.PhoneNumberException("Phone number must start with 9")
        }

        if (!userPhoneNumber.isPhoneNumberEnough()) {
            throw SettingExceptions.PhoneNumberException("Phone number is invalid")
        }



        repository.updatePhoneNumber(phoneNumber)
    }
}
