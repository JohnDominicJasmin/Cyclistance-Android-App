package com.example.cyclistance.feature_rescue_record.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RideDetails(
    val rescuerId: String = "",
    val rescuerName: String = "",
    val rescuerPhotoUrl: String = "",
    val rescueeId: String = "",
    val rescueeName: String = "",
    val rescueePhotoUrl: String = "",
    val rideSummary: RideSummary = RideSummary()
) : Parcelable{
    fun getRating(): Double {
        return rideSummary.rating
    }
    fun getRatingText(): String {
        return rideSummary.ratingText
    }
    fun getTextDescription(): String {
        return rideSummary.textDescription
    }
    fun getBikeType(): String{
        return rideSummary.bikeType
    }
    fun getDate(): String{
        return rideSummary.date
    }
    fun getStartingTime(): String{
        return rideSummary.startingTime
    }
    fun getEndTime(): String{
        return rideSummary.endTime
    }
    fun getStartingAddress(): String{
        return rideSummary.startingAddress
    }
    fun getDestinationAddress(): String{
        return rideSummary.destinationAddress
    }
    fun getDuration(): String{
        return rideSummary.duration
    }
    fun getDistance(): String{
        return rideSummary.distance
    }
    fun getMaxSpeed(): String{
        return rideSummary.maxSpeed
    }

}
