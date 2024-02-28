package com.myapp.cyclistance.core.utils.formatter

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.utils.constants.MappingConstants

object IconFormatter {

    fun String.toHazardousLaneIconMarker(isMarkerYours: Boolean = false): Int{
        return when(this){
            MappingConstants.CONSTRUCTION -> if(isMarkerYours) R.drawable.ic_construction_white else R.drawable.ic_construction_marker
            MappingConstants.LANE_CLOSURE -> if(isMarkerYours) R.drawable.ic_lane_closure_white else R.drawable.ic_lane_closure_marker
            MappingConstants.CRASH -> if(isMarkerYours) R.drawable.ic_crash_white else R.drawable.ic_crash_marker
            MappingConstants.NEED_ASSISTANCE -> if(isMarkerYours) R.drawable.ic_need_assistance_white else R.drawable.ic_need_assistance_marker
            MappingConstants.OBJECT_ON_ROAD -> if(isMarkerYours) R.drawable.ic_object_on_road_white else R.drawable.ic_object_on_road_marker
            MappingConstants.SLOWDOWN -> if(isMarkerYours) R.drawable.ic_slow_down_white else R.drawable.ic_slow_down_marker
            else -> throw RuntimeException("No icon found for $this")
        }
    }


    fun String.rescueDescriptionToIcon(): Int? {
        return when (this) {
            MappingConstants.INJURY_TEXT -> R.drawable.ic_injury
            MappingConstants.BROKEN_FRAME_TEXT -> R.drawable.ic_broken_frame
            MappingConstants.INCIDENT_TEXT -> R.drawable.ic_injury
            MappingConstants.BROKEN_CHAIN_TEXT -> R.drawable.ic_broken_chain
            MappingConstants.FLAT_TIRES_TEXT -> R.drawable.ic_flat_tire
            MappingConstants.FAULTY_BRAKES_TEXT -> R.drawable.ic_faulty_brakes
            else -> null
        }
    }

     fun String.getMapIconImage(): Int? {
        return when (this) {
            MappingConstants.INJURY_TEXT -> R.drawable.ic_injury_em
            MappingConstants.BROKEN_FRAME_TEXT -> R.drawable.ic_broken_frame_em
            MappingConstants.INCIDENT_TEXT -> R.drawable.ic_incident_em
            MappingConstants.BROKEN_CHAIN_TEXT -> R.drawable.ic_broken_chain_em
            MappingConstants.FLAT_TIRES_TEXT -> R.drawable.ic_flat_tire_em
            MappingConstants.FAULTY_BRAKES_TEXT -> R.drawable.ic_faulty_brakes_em
            else -> null

        }
    }


    fun String.getHazardousLaneImage(context: Context, isMarkerYours: Boolean): Drawable? {
        return this.toHazardousLaneIconMarker(isMarkerYours).let { image ->
            AppCompatResources.getDrawable(context, image)
        }
    }


    fun String.getNearbyCyclistImage(context: Context): Drawable? {
        return this.getMapIconImage()?.let { image ->
             AppCompatResources.getDrawable(context, image)
        }
    }


}