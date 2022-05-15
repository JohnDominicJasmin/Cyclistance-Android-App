package com.example.cyclistance.feature_main_screen.domain.model

import com.example.cyclistance.feature_main_screen.data.remote.dto.CancellationReason

data class Cancellation(
    val id: String = "",
    val clientId: String = "",
    val cancellationReasons: List<CancellationReason> = emptyList()

)