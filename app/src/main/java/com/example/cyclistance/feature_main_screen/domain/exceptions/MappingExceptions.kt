package com.example.cyclistance.feature_main_screen.domain.exceptions

object MappingExceptions {
    class UnexpectedErrorException(message:String=""):RuntimeException(message)
    class NoInternetException(message: String = "Couldn't reach server. Check your internet connection"):RuntimeException(message)
    class UnavailablePhoneNumber(message: String  = "Field cannot be blank."):RuntimeException(message)
    class UnavailableName(message: String = "Field cannot be blank."):RuntimeException(message)
}