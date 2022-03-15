package com.example.cyclistance.feature_authentication.domain.use_case

import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class GetNameUseCase(private val repository: AuthRepository<AuthCredential>) {
     operator fun invoke():String{
        return try{
            repository.getName()
        }catch(e: NullPointerException){
            throw AuthExceptions.EmailException(message = "Name not found")
        }
    }
}