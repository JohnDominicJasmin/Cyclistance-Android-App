package com.example.cyclistance.feature_rescue_record.domain.exceptions

object RescueRecordExceptions {
    class InsertRescueRecordException(message: String = "Insert rescue record failed") : RuntimeException()
    class GetRescueRecordException(message: String = "Get rescue record failed") : RuntimeException()
    class GetRideHistoryException(message: String = "Get ride history failed") : RuntimeException()
    class RateRescuerException(message: String = "Failed to rate rescuer") : RuntimeException()
    class UpdateUserStatsException(message: String = "Failed to update user stats") : RuntimeException()
    class AddRideMetricsException(message: String = "Failed to update user stats") : RuntimeException()
}