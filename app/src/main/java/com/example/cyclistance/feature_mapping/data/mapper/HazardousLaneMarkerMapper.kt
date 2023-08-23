package com.example.cyclistance.feature_mapping.data.mapper

import com.example.cyclistance.core.utils.constants.MappingConstants.KEY_DATE_POSTED
import com.example.cyclistance.core.utils.constants.MappingConstants.KEY_ID
import com.example.cyclistance.core.utils.constants.MappingConstants.KEY_ID_CREATOR
import com.example.cyclistance.core.utils.constants.MappingConstants.KEY_ID_LABEL
import com.example.cyclistance.core.utils.constants.MappingConstants.KEY_ID_LATITUDE
import com.example.cyclistance.core.utils.constants.MappingConstants.KEY_ID_LONGITUDE
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLaneMarker
import com.google.firebase.firestore.DocumentSnapshot

object HazardousLaneMarkerMapper {

    fun DocumentSnapshot.toHazardousLaneMarker(): HazardousLaneMarker {
        return HazardousLaneMarker(
            id = getString(KEY_ID)!!,
            idCreator = getString(KEY_ID_CREATOR)!!,
            latitude = getDouble(KEY_ID_LATITUDE),
            longitude = getDouble(KEY_ID_LONGITUDE),
            label = getString(KEY_ID_LABEL)!!,
            datePosted = getDate(KEY_DATE_POSTED)!!,
        )
    }

}