package com.example.cyclistance.feature_authentication.domain.use_case.create_account

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.utils.ConnectionStatus
import com.example.cyclistance.utils.InputValidate.isEmailValid
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class SignInWithEmailAndPasswordUseCase(
    private val context: Context,
    private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke(authModel: AuthModel):Boolean {

        return when {
            authModel.email.trim().isEmpty() ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.fieldLeftBlankMessage))

            !isEmailValid(authModel.email.trim()) ->
                throw AuthExceptions.EmailException(message = context.getString(R.string.emailIsInvalidMessage))

            authModel.password.trim().isEmpty() ->
                throw AuthExceptions.PasswordException(message = context.getString(R.string.fieldLeftBlankMessage))

            !ConnectionStatus.hasInternetConnection(context) ->
                throw AuthExceptions.InternetException(message = context.getString(R.string.no_internet_message))

            else -> repository.signInWithEmailAndPassword(
                authModel.email.trim(),
                authModel.password.trim())
        }
    }
}