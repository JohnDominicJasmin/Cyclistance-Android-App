package com.myapp.cyclistance.feature_authentication.domain.use_case.create_account

import com.myapp.cyclistance.core.utils.resource_texts.ResourceText
import com.myapp.cyclistance.core.utils.validation.InputValidate.isEmailValid
import com.myapp.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.myapp.cyclistance.feature_authentication.domain.model.AuthModel
import com.myapp.cyclistance.feature_authentication.domain.repository.AuthRepository

class SignInWithEmailAndPasswordUseCase(
    private val repository: AuthRepository) {

    suspend operator fun invoke(authModel: AuthModel): Boolean {

        val email = authModel.email.trim()
        val password = authModel.password.trim()

        return when {
            email.isEmpty() ->
                throw AuthExceptions.EmailException(message = ResourceText.FieldLeftBlank().message)

            !email.isEmailValid() ->
                throw AuthExceptions.EmailException(message = ResourceText.EmailIsInvalid().message)

            password.isEmpty() ->
                throw AuthExceptions.NewPasswordException(message = ResourceText.FieldLeftBlank().message)


            else -> repository.signInWithEmailAndPassword(email, password)
        }
    }
}