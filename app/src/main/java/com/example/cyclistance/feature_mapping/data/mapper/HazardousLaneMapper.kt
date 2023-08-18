package com.example.cyclistance.feature_mapping.data.mapper

import com.example.cyclistance.feature_mapping.data.data_source.network.dto.hazardous_lane.HazardousLaneDto
import com.example.cyclistance.feature_mapping.domain.model.remote_models.hazardous_lane.HazardousLane

object HazardousLaneMapper {
    fun HazardousLaneDto.toHazardousLane():  HazardousLane{
        return HazardousLane(
            markers = this
        )
    }


}