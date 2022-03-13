package com.example.cyclistance.feature_authentication.domain.use_case

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.common.ConnectionStatus
import com.example.cyclistance.common.InputValidate.strongPassword
import com.example.cyclistance.common.InputValidate.validEmail

import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthInputModel
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class CreateWithEmailAndPasswordUseCase(
    private val context: Context,
    private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke(authInputModel: AuthInputModel): Boolean {

        return when {
            ConnectionStatus.hasInternetConnection(context) == true ->
                throw AuthExceptions.NoInternetException(message = context.getString(R.string.no_internet_message))

            authInputModel.password.isEmpty() ->
                throw AuthExceptions.PasswordException(message = context.getString(R.string.fieldLeftBlankMessage))

            authInputModel.confirmPassword.isEmpty() ->
                throw AuthExceptions.ConfirmPasswordException(message = context.getString(R.string.fieldLeftBlankMessage))

            authInputModel.password !=
                    authInputModel.confirmPassword ->
                throw AuthExceptions.ConfirmPasswordException(message = context.getString(R.string.passwordIsNotMatchMessage))

            !strongPassword(authInputModel) ->
                throw AuthExceptions.ConfirmPasswordException(message = context.getString(R.string.passwordIsWeakMessage))

            authInputModel.email.isEmpty() ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.fieldLeftBlankMessage))

            !validEmail(authInputModel.email) ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.emailIsInvalidMessage))

            else -> repository.createUserWithEmailAndPassword(
                authInputModel.email,
                authInputModel.password)
        }
    }



}