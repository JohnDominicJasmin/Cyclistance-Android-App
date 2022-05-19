package com.example.cyclistance.feature_main_screen.domain.model

import com.example.cyclistance.feature_main_screen.data.remote.dto.CancellationReason
import com.google.gson.annotations.SerializedName

data class CancellationEvent(
    val cancellationReason: List<CancellationReason>,
    val clientId: String,
    val id: String
)
