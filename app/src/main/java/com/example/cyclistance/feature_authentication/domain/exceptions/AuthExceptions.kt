package com.example.cyclistance.feature_authentication.domain.exceptions

sealed class AuthExceptions{
    class InternetException(message: String):RuntimeException(message)
    class EmailVerificationException(message:String):RuntimeException(message)
    class InvalidUserException(message:String):RuntimeException(message)
    class ConflictFBTokenException(message:String):RuntimeException(message)
    class PasswordException(message:String):RuntimeException(message)
    class ConfirmPasswordException(message:String):RuntimeException(message)
    class EmailException(message:String):RuntimeException(message)
}
