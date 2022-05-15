package com.example.cyclistance.feature_main_screen.data.remote.dto

data class CancellationDto(
    val id: String = "",
    val clientId: String = "",
    val cancellationReasons: List<CancellationReason> = emptyList()
    )
