package com.example.cyclistance.feature_emergency_call.domain.exceptions

object EmergencyCallExceptions {
    class NameException(message: String = "Field cannot be blank") : RuntimeException(message)
    class PhoneNumberException(message: String = "Field cannot be blank") :
        RuntimeException(message)
}
