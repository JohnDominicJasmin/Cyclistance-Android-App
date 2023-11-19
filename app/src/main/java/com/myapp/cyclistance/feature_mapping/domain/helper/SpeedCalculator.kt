package com.myapp.cyclistance.feature_mapping.domain.helper

class SpeedCalculator {
     fun calculateAverageSpeedInMeters(distanceMeters: Double, timeMillis: Long): Double {
        val timeSeconds = timeMillis / 1000.0  // Convert milliseconds to seconds
        return distanceMeters / timeSeconds
    }

}