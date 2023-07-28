package com.example.cyclistance.navigation.event

sealed class NavUiEvent{
    data class OnChangeNavigation(val isNavigating:Boolean): NavUiEvent()
    object OnToggleTheme: NavUiEvent()
    data class NewConversationDetails(val chatName: String, val chatPhotoUrl: String): NavUiEvent()
}
