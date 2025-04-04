package com.myapp.cyclistance.feature_rescue_record.data.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.myapp.cyclistance.core.utils.date.DateUtils.toReadableDateTime
import com.myapp.cyclistance.feature_mapping.domain.model.Role
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideHistoryItem

object RideHistoryMapper {
    fun DocumentSnapshot.toRideHistoryItem(uid: String): RideHistoryItem {

        val rescuerId = this["rescuerId"].toString()
        val isRescuer = uid == rescuerId
        val role = if (isRescuer) Role.Rescuer.name else Role.Rescuee.name
        val rideId = this["rideId"].toString()
        val photoUrl =
            if (isRescuer) this["rescueePhotoUrl"].toString() else this["rescuerPhotoUrl"].toString()
        val date = this.getDate("rideDate")?.toReadableDateTime(pattern = "MMM dd, yyyy hh:mm a")
        val duration = this["rideSummary.duration"].toString()
        val rescueDescription = this["rideSummary.iconDescription"].toString()
        val startingAddress = this["rideSummary.startingAddress"].toString()
        val destinationAddress = this["rideSummary.destinationAddress"].toString()



        return RideHistoryItem(
            role = role,
            id = rideId,
            photoUrl = photoUrl,
            date = date ?: "Date not available",
            duration = duration,
            rescueDescription = rescueDescription,
            startingAddress = startingAddress,
            destinationAddress = destinationAddress,
        )
    }
}