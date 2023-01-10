package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.validation.InputValidate.isEmailValid
import com.example.cyclistance.core.utils.validation.InputValidate.isPasswordStrong
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.hasInternetConnection

class CreateWithEmailAndPasswordUseCase(
    private val context: Context,
    private val repository: AuthRepository) {

    suspend operator fun invoke(authModel: AuthModel): Boolean {

        val email = authModel.email.trim()
        val password = authModel.password.trim()
        val confirmPassword = authModel.confirmPassword.trim()


        return when {

            email.isEmpty() ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.fieldLeftBlankMessage))

            !email.isEmailValid() ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.emailIsInvalidMessage))

            password.isEmpty() ->
                throw AuthExceptions.PasswordException(message = context.getString(R.string.fieldLeftBlankMessage))

            confirmPassword.isEmpty() ->
                throw AuthExceptions.ConfirmPasswordException(message = context.getString(R.string.fieldLeftBlankMessage))

            password != confirmPassword ->
                throw AuthExceptions.ConfirmPasswordException(message = context.getString(R.string.passwordIsNotMatchMessage))

            !confirmPassword.isPasswordStrong() ->
                throw AuthExceptions.ConfirmPasswordException(message = context.getString(R.string.passwordIsWeakMessage))

            !context.hasInternetConnection() ->
                throw AuthExceptions.NetworkException(message = context.getString(R.string.no_internet_message))

            else -> repository.createUserWithEmailAndPassword(email, password)
        }
    }



}