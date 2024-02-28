package com.myapp.cyclistance.feature_authentication.domain.use_case.create_account

import com.myapp.cyclistance.core.utils.resource_texts.ResourceText
import com.myapp.cyclistance.core.utils.validation.InputValidate.isEmailValid
import com.myapp.cyclistance.core.utils.validation.InputValidate.isPasswordStrong
import com.myapp.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.myapp.cyclistance.feature_authentication.domain.model.AuthModel
import com.myapp.cyclistance.feature_authentication.domain.model.AuthenticationResult
import com.myapp.cyclistance.feature_authentication.domain.repository.AuthRepository

class CreateWithEmailAndPasswordUseCase(
    private val repository: AuthRepository) {

    suspend operator fun invoke(authModel: AuthModel): AuthenticationResult? {

        val email = authModel.email.trim()
        val password = authModel.password.trim()
        val confirmPassword = authModel.confirmPassword.trim()


        return when {

            email.isEmpty() ->
                throw AuthExceptions.EmailException(message = ResourceText.FieldLeftBlank().message)

            !email.isEmailValid() ->
                throw AuthExceptions.EmailException(message = ResourceText.EmailIsInvalid().message)

            password.isEmpty() ->
                throw AuthExceptions.NewPasswordException(message = ResourceText.FieldLeftBlank().message)

            confirmPassword.isEmpty() ->
                throw AuthExceptions.ConfirmPasswordException(message = ResourceText.FieldLeftBlank().message)

            password != confirmPassword ->
                throw AuthExceptions.ConfirmPasswordException(message = ResourceText.PasswordNotMatch().message)

            !confirmPassword.isPasswordStrong() ->
                throw AuthExceptions.ConfirmPasswordException(message = ResourceText.PasswordWeak().message)


            else -> repository.createUserWithEmailAndPassword(email, password)
        }
    }



}