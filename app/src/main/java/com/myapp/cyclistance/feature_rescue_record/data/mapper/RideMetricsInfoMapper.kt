package com.myapp.cyclistance.feature_rescue_record.data.mapper

import com.myapp.cyclistance.feature_rescue_record.data.local.entities.RideMetricsInfo
import com.myapp.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics

object RideMetricsInfoMapper {

        fun RideMetricsInfo.toRideMetrics(): RideMetrics {
            return RideMetrics(
                distanceInMeters = this.distanceInMeters,
                maxSpeed = this.maxSpeed,
                averageSpeedMps = this.averageSpeedMps
            )
        }

        fun RideMetrics.toRideMetricsInfo(): RideMetricsInfo {
            return RideMetricsInfo(
                distanceInMeters = this.distanceInMeters,
                maxSpeed = this.maxSpeed,
                averageSpeedMps = this.averageSpeedMps
            )
        }
}