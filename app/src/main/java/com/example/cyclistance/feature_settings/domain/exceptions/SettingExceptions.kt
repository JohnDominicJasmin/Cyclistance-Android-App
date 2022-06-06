package com.example.cyclistance.feature_settings.domain.exceptions

import com.example.cyclistance.feature_readable_displays.domain.exceptions.ReadableDisplaysExceptions

sealed class SettingExceptions(private val exceptionMessage: String):RuntimeException(exceptionMessage){
    class UnexpectedErrorException(message:String): SettingExceptions(message)
}
