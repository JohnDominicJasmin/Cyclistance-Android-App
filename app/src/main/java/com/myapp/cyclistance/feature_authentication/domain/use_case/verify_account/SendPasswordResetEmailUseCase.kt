package com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account

import com.myapp.cyclistance.core.utils.resource_texts.ResourceText
import com.myapp.cyclistance.core.utils.validation.InputValidate.isEmailValid
import com.myapp.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.myapp.cyclistance.feature_authentication.domain.repository.AuthRepository

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