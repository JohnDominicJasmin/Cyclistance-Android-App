package com.example.cyclistance.feature_messaging.domain.exceptions

object MessagingExceptions {
    class NetworkException(message: String) : RuntimeException(message)
    class TokenException(message: String) : RuntimeException(message)
    class SendMessagingFailure(message: String) : RuntimeException(message)
    class ListenMessagingFailure(message: String) : RuntimeException(message)
    class GetMessageUsersFailure(message: String) : RuntimeException(message)
}