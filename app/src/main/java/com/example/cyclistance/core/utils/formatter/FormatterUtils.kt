package com.example.cyclistance.core.utils.formatter

import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

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

    fun toRescueDescription(description: String): String? {
        return when (description) {
            MappingConstants.INJURY_TEXT -> "injuryCount"
            MappingConstants.BROKEN_FRAME_TEXT -> "frameSnapCount"
            MappingConstants.INCIDENT_TEXT -> "incidentCount"
            MappingConstants.BROKEN_CHAIN_TEXT -> "brokenChainCount"
            MappingConstants.FLAT_TIRES_TEXT -> "flatTireCount"
            MappingConstants.FAULTY_BRAKES_TEXT -> "faultyBrakesCount"
            else -> null
        }
    }

    fun metersToKilometerPerHour(metersPerSecond: Double): Double {
        return metersPerSecond * 3.6
    }

    // TODO: test this code
    fun LocationModel?.isLocationAvailable() = (this?.latitude != null).and(this?.longitude != null)



    fun getTimeDurationMillis(startingMillis: Long, endingMillis: Long): Long {
        return kotlin.math.abs(endingMillis - startingMillis)
    }

    fun formatDuration(startingMillis: Long, endingMillis: Long): String {
        val duration = getTimeDurationMillis(startingMillis, endingMillis)
        Timber.v("Current millis: $duration")
        val hours = TimeUnit.MILLISECONDS.toHours(duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours)

        return when {
            hours > 0 -> "${hours}h ${minutes}m ${seconds}s"
            minutes > 0 -> "${minutes}m ${seconds}s"
            else -> "${seconds}s"
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



}