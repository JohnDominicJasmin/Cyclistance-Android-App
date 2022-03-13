package com.example.cyclistance.feature_authentication.domain.exceptions

sealed class AuthExceptions(m: String):RuntimeException(m){
    class NoInternetException(message: String):AuthExceptions(message)
    class EmailVerificationException(message:String):AuthExceptions(message)
    class InvalidUserException(message:String):AuthExceptions(message)
    class ConflictFBTokenException(message:String):AuthExceptions(message)
    class PasswordException(message:String):AuthExceptions(message)
    class ConfirmPasswordException(message:String):AuthExceptions(message)
    class EmailException(message:String):AuthExceptions(message)
}
