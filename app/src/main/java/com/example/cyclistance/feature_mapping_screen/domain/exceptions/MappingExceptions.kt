package com.example.cyclistance.feature_mapping_screen.domain.exceptions

object MappingExceptions {
    class UnexpectedErrorException(message:String="An unexpected error occurred."):RuntimeException(message)
    class NetworkException(message: String = "Couldn't reach server. Check your internet connection"):RuntimeException(message)
    class UserException(message: String = "User not found"):RuntimeException(message)
    class PhoneNumberException(message: String  = "Field cannot be blank"):RuntimeException(message)
    class NameException(message: String = "Field cannot be blank"):RuntimeException(message)
    class BikeTypeException(message: String = "Select bike type"): RuntimeException(message)
    class AddressException(message: String = "Field cannot be blank"): RuntimeException(message)
    class DescriptionException(message: String = "Select help description"): RuntimeException(message)
    class RescueTransactionIdException(message: String = "Rescue transaction not found"): RuntimeException(message)
    class RescueTransactionReasonException(message: String = "Select cancellation reason"): RuntimeException(message)
}