package com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideMetrics
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideSummary

sealed class RescueDetailsEvent{
    data class GetRideSummarySuccess(val rideSummary: RideSummary): RescueDetailsEvent()
    data class GetRescueRecordFailed(val message: String): RescueDetailsEvent()

    data class GetRideMetricsSuccess(val rideMetrics: RideMetrics?): RescueDetailsEvent()
}
