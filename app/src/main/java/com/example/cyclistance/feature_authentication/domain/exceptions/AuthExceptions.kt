package com.example.cyclistance.feature_authentication.domain.exceptions

sealed class AuthExceptions {
    class NetworkException(message: String) : RuntimeException(message)
    class UserAlreadyExistsException(val title: String, message: String) : RuntimeException(message)
    class EmailVerificationException(message: String) : RuntimeException(message)
    class TooManyRequestsException(val title: String, message: String) : RuntimeException(message)
    class ConflictFBTokenException(message: String) : RuntimeException(message)
    class CurrentPasswordException(message: String) : RuntimeException(message)
    class NewPasswordException(message: String) : RuntimeException(message)
    class ConfirmPasswordException(message: String) : RuntimeException(message)
    class EmailException(message: String) : RuntimeException(message)
    class UserException(message: String = "User not found") : RuntimeException(message)
    class CreateUserException(message: String = "Error creating user") : RuntimeException(message)
    class AccountDisabledException(message: String = "") : RuntimeException(message)
}
