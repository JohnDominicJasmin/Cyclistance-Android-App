package com.example.cyclistance.feature_readable_displays.domain.exceptions

sealed class ReadableDisplaysExceptions(private val exceptionMessage: String):RuntimeException(exceptionMessage){
    class UnexpectedErrorException(message:String):ReadableDisplaysExceptions(message)
}

