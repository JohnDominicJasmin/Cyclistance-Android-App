package com.example.cyclistance.feature_authentication.domain.use_case.verify_account

import com.example.cyclistance.core.utils.resource_texts.ResourceText
import com.example.cyclistance.core.utils.validation.InputValidate.isEmailValid
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository

class SendPasswordResetEmailUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String) {

        if(email.isEmpty()) {
            throw AuthExceptions.EmailException(message = ResourceText.FieldLeftBlank().message)
        }
        if(!email.isEmailValid()){
            throw AuthExceptions.EmailException(message = ResourceText.EmailIsInvalid().message)
        }

        repository.sendPasswordResetEmail(email)
    }
}