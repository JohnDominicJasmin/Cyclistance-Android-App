package com.example.cyclistance.feature_main_screen.data.remote.dto

data class Status(
    val isSearching: Boolean = false,
    val isStarted: Boolean = false,
    val onGoing: Boolean = false,
    val isFinished: Boolean = false
)
