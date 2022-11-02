package com.example.cyclistance.feature_mapping_screen.domain.model

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.Cancellation
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.Status

data class RescueTransaction(
    val id: String? = null,
    val cancellation: Cancellation? = null,
    val rescueeId: String? = null,
    val rescuerId: String? = null,
    val status: Status? = null
)
