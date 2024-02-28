package com.myapp.cyclistance.feature_authentication.domain.use_case.verify_account

import com.myapp.cyclistance.core.utils.validation.InputValidate.isPasswordStrong
import com.myapp.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.myapp.cyclistance.feature_authentication.domain.repository.AuthRepository

class ChangePasswordUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(
        currentPassword: String,
        newPassword: String,
        confirmPassword: String) {


        if (currentPassword.isEmpty()) {
            throw AuthExceptions.CurrentPasswordException(message = "Field cannot be left blank")
        }

        if (newPassword.isEmpty()) {
            throw AuthExceptions.NewPasswordException(message = "Field cannot be left blank")
        }

        if (confirmPassword.isEmpty()) {
            throw AuthExceptions.ConfirmPasswordException(message = "Field cannot be left blank")
        }

        if(newPassword != confirmPassword){
            throw AuthExceptions.ConfirmPasswordException(message = "Passwords do not match")
        }

        if (!confirmPassword.isPasswordStrong()){
            throw AuthExceptions.ConfirmPasswordException(message = "Password is too weak")
        }

        repository.changePassword(
            currentPassword = currentPassword,
            confirmPassword = confirmPassword)

    }
}