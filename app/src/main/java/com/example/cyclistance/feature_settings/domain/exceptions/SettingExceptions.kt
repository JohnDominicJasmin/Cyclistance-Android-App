package com.example.cyclistance.feature_settings.domain.exceptions

sealed class SettingExceptions{
    class PhoneNumberException(message: String  = "Field cannot be blank"):RuntimeException(message)
    class NameException(message: String = "Field cannot be blank"):RuntimeException(message)
    class NetworkException(message: String) : RuntimeException(message)
    class InternalServerException(message: String) : RuntimeException(message)
}
