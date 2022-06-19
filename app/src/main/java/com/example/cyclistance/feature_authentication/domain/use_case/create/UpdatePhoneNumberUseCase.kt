package com.example.cyclistance.feature_authentication.domain.use_case.create

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import com.google.firebase.auth.AuthCredential

class UpdatePhoneNumberUseCase(private val repository: AuthRepository<AuthCredential>) {
    suspend operator fun invoke(phoneNumber:String){
        if(phoneNumber.isEmpty()){
            throw MappingExceptions.PhoneNumberException()
        }
        repository.updatePhoneNumber(phoneNumber)
    }
}