package com.example.cyclistance.navigation.event

sealed class NavUiEvent{
    data class OnChangeNavigation(val isNavigating:Boolean): NavUiEvent()
    data object OnToggleTheme: NavUiEvent()
}
