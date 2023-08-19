package com.example.cyclistance.core.utils.validation

import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatterUtils {
    private const val METER_TO_KILOMETER = 1000

    fun Double.formatToDistanceKm(): String {

        return if (this <= 0.0) {
            "0 m"
        } else if (this < 1000) {
            "%.2f m".format(this)
        } else {
            "%.2f km".format((this / METER_TO_KILOMETER))
        }
    }

    fun List<UserItem>.findUser(id: String): UserItem {
        return find { it.id == id } ?: UserItem()
    }


    // TODO: test this code
    fun LocationModel?.isLocationAvailable() = (this?.latitude != null).and(this?.longitude != null)

    fun getCalculatedETA(
        distanceMeters: Double,
        averageSpeedKm: Double = MappingConstants.DEFAULT_BIKE_AVERAGE_SPEED_KM): String {
        val distanceToKm = distanceMeters / 1000
        if (distanceToKm <= 0.0) {
            return "0 min"
        }
        val eta = distanceToKm / averageSpeedKm
        val hours = eta.toInt()
        val minutes = (eta - hours) * 60
        val minutesInt = minutes.toInt()
        val minsFormat = if (minutesInt <= 1) "$minutesInt min" else "$minutesInt mins"
        val hourFormat = if (hours >= 1) "$hours hrs " else ""
        return "$hourFormat$minsFormat"
    }

    // TODO: test this code
    fun Date.toReadableDateTime(pattern: String = "dd/MM/yyyy HH:mm"): String {
        return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
    }
}