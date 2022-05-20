package com.example.cyclistance.feature_main_screen.domain.exceptions

object CustomExceptions {
    class UnexpectedErrorException(message:String):RuntimeException(message)
    class NoInternetException(message: String):RuntimeException(message)
}