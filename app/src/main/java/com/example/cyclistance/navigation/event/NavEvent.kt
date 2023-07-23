package com.example.cyclistance.navigation.event

sealed class NavEvent {
    object DeleteMessagingTokenSuccess : NavEvent()
    data class DeleteMessagingTokenFailure(val message: String) : NavEvent()
}
