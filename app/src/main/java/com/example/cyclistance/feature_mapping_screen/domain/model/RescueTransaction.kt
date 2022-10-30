package com.example.cyclistance.feature_mapping_screen.domain.model

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.Cancellation
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.Status

data class RescueTransaction(
    val id: String = "",
    val cancellation: Cancellation = Cancellation(),
    val rescueeId: String = "",
    val rescuerId: String = "",
    val status: Status = Status()
)
