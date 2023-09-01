package com.example.cyclistance.feature_user_profile.domain.exceptions

sealed class UserProfileExceptions{
    class NameException(message: String = "Field cannot be blank"):RuntimeException(message)
    class AddressException(message: String = "Field cannot be blank") : RuntimeException(message)
    class NetworkException(message: String) : RuntimeException(message)
    class InternalServerException(message: String) : RuntimeException(message)
    class UpdateProfileException(message: String) : RuntimeException(message)
    class GetProfileException(message: String) : RuntimeException(message)
    class UpdateActivityException(message: String) : RuntimeException(message)
    class UpdateReasonAssistanceException(message: String) : RuntimeException(message)
}
