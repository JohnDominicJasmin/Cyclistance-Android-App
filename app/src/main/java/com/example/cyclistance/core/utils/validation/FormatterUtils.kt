package com.example.cyclistance.core.utils.validation

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping.domain.model.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.UserItem

object FormatterUtils {


    fun Double.formatToDistanceKm(): String {

        return if(this <= 0.0) {
            "0 m"
        } else if (this < 1000) {
            "%.2f m".format(this)
        } else {
            "%.2f km".format((this / 1000))
        }
    }

     fun RescueTransaction.findRescueTransaction(id: String): RescueTransactionItem {
        return this.transactions.find {
            it.id == id
        } ?: RescueTransactionItem()
    }

    fun NearbyCyclist.findUser(id: String): UserItem {
        return this.users.find {
            it.id == id
        } ?: UserItem()
    }

    fun Location?.isLocationAvailable() = (this?.latitude != null).and(this?.longitude != null)

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