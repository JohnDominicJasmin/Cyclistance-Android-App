package com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction


import com.google.gson.annotations.SerializedName

data class RescueTransactionItemDto(
    @SerializedName("cancellation")
    val cancellation: CancellationDto?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("rescuee_id")
    val rescueeId: String?,
    @SerializedName("rescuer_id")
    val rescuerId: String?,
    @SerializedName("status")
    val status: StatusDto?,
    @SerializedName("route")
    val route: RouteDto?
)