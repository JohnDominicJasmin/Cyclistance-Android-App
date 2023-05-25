package com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction

import android.os.Parcelable
import androidx.annotation.Keep
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.LocationDto
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RouteDto(
    @SerializedName("starting_location")
    val startingLocation: LocationDto = LocationDto(),
    @SerializedName("destination_location")
    val destinationLocation: LocationDto = LocationDto()


    ):Parcelable
