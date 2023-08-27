package com.example.cyclistance.core.utils.formatter

import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object FormatterUtils {

    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
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




    // TODO: test this code
    fun LocationModel?.isLocationAvailable() = (this?.latitude != null).and(this?.longitude != null)

    private fun currentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    fun formatToTimeAgo(timeMillis: Long): String {
        var time = Date(timeMillis).time
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = currentDate().time
        if (time > now || time <= 0) {
            return "in the future"
        }

        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> "just now"
            diff < 2 * MINUTE_MILLIS -> "1m ago"
            diff < 60 * MINUTE_MILLIS -> "${diff / MINUTE_MILLIS}m"
            diff < 2 * HOUR_MILLIS -> "1h ago"
            diff < 24 * HOUR_MILLIS -> "${diff / HOUR_MILLIS}h ago"
            diff < 48 * HOUR_MILLIS -> "1d ago"
            else -> "${diff / DAY_MILLIS}d ago"
        }
    }


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