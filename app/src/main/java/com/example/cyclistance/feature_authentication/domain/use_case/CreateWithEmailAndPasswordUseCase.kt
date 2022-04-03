package com.example.cyclistance.feature_authentication.domain.use_case

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.common.ConnectionStatus
import com.example.cyclistance.common.InputValidate
import com.example.cyclistance.common.InputValidate.isPasswordStrong
import com.example.cyclistance.common.InputValidate.isEmailValid

import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class CreateWithEmailAndPasswordUseCase(
    private val context: Context,
    private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke(authModel: AuthModel): Boolean {

        return when {

            authModel.email.isEmpty() ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.fieldLeftBlankMessage))

            !isEmailValid(authModel.email) ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.emailIsInvalidMessage))

            authModel.password.isEmpty() ->
                throw AuthExceptions.PasswordException(message = context.getString(R.string.fieldLeftBlankMessage))

            authModel.confirmPassword.isEmpty() ->
                throw AuthExceptions.ConfirmPasswordException(message = context.getString(R.string.fieldLeftBlankMessage))

            authModel.password !=
                    authModel.confirmPassword ->
                throw AuthExceptions.ConfirmPasswordException(message = context.getString(R.string.passwordIsNotMatchMessage))

            !isPasswordStrong(authModel.confirmPassword) ->
                throw AuthExceptions.ConfirmPasswordException(message = context.getString(R.string.passwordIsWeakMessage))

            !ConnectionStatus.hasInternetConnection(context) ->
                throw AuthExceptions.InternetException(message = context.getString(R.string.no_internet_message))

            else -> repository.createUserWithEmailAndPassword(
                authModel.email,
                authModel.password)
        }
    }



}