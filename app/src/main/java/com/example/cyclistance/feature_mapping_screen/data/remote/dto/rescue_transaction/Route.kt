package com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction

import android.os.Parcelable
import androidx.annotation.Keep
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Location
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Route(
    @SerializedName("starting_location")
    val startingLocation: Location = Location(),
    @SerializedName("destination_location")
    val destinationLocation: Location = Location()


    ):Parcelable
