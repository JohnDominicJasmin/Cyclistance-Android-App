package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserItemDto(
    @SerializedName("address")
    val address: String?,
    @SerializedName("contact_number")
    val contactNumber: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("location")
    val location: LocationDto?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("profile_picture_url")
    val profilePictureUrl: String?,
    @SerializedName("rescue_request")
    val rescueRequest: RescueRequestDto?,
    @SerializedName("transaction")
    val transaction: TransactionDto?,
    @SerializedName("user_assistance")
    val userAssistance: UserAssistanceDto?
):Parcelable

