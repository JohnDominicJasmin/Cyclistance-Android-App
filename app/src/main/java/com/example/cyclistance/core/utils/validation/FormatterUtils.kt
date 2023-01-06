package com.example.cyclistance.core.utils.validation

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.appcompat.content.res.AppCompatResources
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import im.delight.android.location.SimpleLocation
import timber.log.Timber
import java.io.IOException

object FormatterUtils {

    @Suppress("DEPRECATION")
    @WorkerThread
    inline fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        crossinline onCallbackAddress: (Address?) -> Unit) {

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getFromLocation(
                    latitude, longitude, 1,
                ) { addresses ->
                    onCallbackAddress(addresses.lastOrNull())
                }
            } else {
                onCallbackAddress(getFromLocation(latitude, longitude, 1)?.lastOrNull())
            }

        } catch (e: IOException) {
            Timber.e("GET ADDRESS: ${e.message}")
        }
    }


    fun Address.getFullAddress(): String {
        val subThoroughfare = if (subThoroughfare != "null" && subThoroughfare != null) "$subThoroughfare " else ""
        val thoroughfare = if (thoroughfare != "null" && thoroughfare != null) "$thoroughfare., " else ""
        val subAdminArea = if (subAdminArea != "null" && subAdminArea != null) subAdminArea else ""

        val locality = if (locality != "null" && locality != null) "$locality, " else ""
        val formattedLocality = if(subAdminArea.isNotEmpty()) locality else locality.replace(
            oldChar = ',',
            newChar = ' ',
            ignoreCase = true
        )

        return "$subThoroughfare$thoroughfare$formattedLocality$subAdminArea"
    }
    fun Double.distanceFormat(): String {

        return if(this <= 0.0) {
            "0 m"
        } else if (this < 1000) {
            "%.2f m".format(this)
        } else {
            "%.2f km".format((this / 1000))
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

    fun getETABetweenTwoPoints(startingLocation: Location, endLocation: Location): String {
        val distance = getCalculatedDistance(startingLocation, endLocation)
        return getCalculatedETA(distance)
    }

    /**
    Returns distance in meters
     **/
    fun getCalculatedDistance(startingLocation: Location, endLocation: Location): Double {
        val start = SimpleLocation.Point(startingLocation.latitude, startingLocation.longitude)
        val end = SimpleLocation.Point(endLocation.latitude, endLocation.longitude)
        return SimpleLocation.calculateDistance(start, end)
    }


    fun getCalculatedDistance(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double): Double {
        return SimpleLocation.calculateDistance(
            SimpleLocation.Point(startLatitude, startLongitude),
            SimpleLocation.Point(endLatitude, endLongitude)
        )
    }

    fun String.getMapIconImageDescription(context: Context): Drawable? {
      this.getMapIconImage()?.let { image ->
          return AppCompatResources.getDrawable(context, image)
      }
        return null
    }


    fun String.getMapIconImage():Int?{
        return when(this){
            MappingConstants.INJURY_TEXT -> {
                R.drawable.ic_injury_em
            }
            MappingConstants.BROKEN_FRAME_TEXT -> {
                R.drawable.ic_broken_frame_em
            }
            MappingConstants.INCIDENT_TEXT -> {
                R.drawable.ic_incident_em
            }
            MappingConstants.BROKEN_CHAIN_TEXT -> {
                R.drawable.ic_broken_chain_em
            }
            MappingConstants.FLAT_TIRES_TEXT -> {
                R.drawable.ic_flat_tire_em
            }
            MappingConstants.FAULTY_BRAKES_TEXT -> {
                R.drawable.ic_faulty_brakes_em
            }

            else -> null
        }
    }
}