package com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction


import com.google.gson.annotations.SerializedName

data class CancellationDto(
    @SerializedName("cancellation_reason")
    val cancellationReason: CancellationReasonDto = CancellationReasonDto(),
    @SerializedName("id_cancelled_by")
    val idCancelledBy: String = "",
    @SerializedName("name_cancelled_by")
    val nameCancelledBy: String = "",
    @SerializedName("rescue_cancelled")
    val rescueCancelled: Boolean = false
)