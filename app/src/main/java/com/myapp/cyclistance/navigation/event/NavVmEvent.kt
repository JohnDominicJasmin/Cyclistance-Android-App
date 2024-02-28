package com.myapp.cyclistance.navigation.event

sealed class NavVmEvent {
    object DeleteMessagingToken : NavVmEvent()
}