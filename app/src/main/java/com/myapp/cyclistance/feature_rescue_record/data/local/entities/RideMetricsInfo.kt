package com.myapp.cyclistance.feature_rescue_record.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RideMetricsInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val distanceInMeters: Double = 0.0,
    val maxSpeed: String = "",
    val averageSpeedMps: Double = 0.0
)
