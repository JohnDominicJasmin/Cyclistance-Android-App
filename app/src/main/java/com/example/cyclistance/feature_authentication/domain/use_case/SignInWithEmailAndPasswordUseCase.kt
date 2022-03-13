package com.example.cyclistance.feature_authentication.domain.use_case

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.common.ConnectionStatus
import com.example.cyclistance.common.InputValidate.validEmail
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthInputModel
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class SignInWithEmailAndPasswordUseCase(
    private val context: Context,
    private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke(authInputModel: AuthInputModel):Boolean {

        return when {
            ConnectionStatus.hasInternetConnection(context) == true ->
                throw AuthExceptions.NoInternetException(message = context.getString(R.string.no_internet_message))

            authInputModel.password.isEmpty() ->
                throw AuthExceptions.PasswordException(message = context.getString(R.string.fieldLeftBlankMessage))

            authInputModel.email.isEmpty() ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.fieldLeftBlankMessage))

            !validEmail(authInputModel.email) ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.emailIsInvalidMessage))

            else -> repository.signInWithEmailAndPassword(
                authInputModel.email,
                authInputModel.password)
        }
    }
}