package com.myapp.cyclistance.feature_rescue_record.domain.use_case

import com.myapp.cyclistance.feature_rescue_record.domain.use_case.metrics.AddRideMetricsUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.rate.RateRescueUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.rate.RateRescuerUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.rescue_record.AddRescueRecordUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.rescue_record.GetRescueRecordUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.ride_details.RideDetailsUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.ride_metrics.RideMetricsUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.ridehistory.GetRideHistoryUseCase
import com.myapp.cyclistance.feature_rescue_record.domain.use_case.stats.UpdateStatsUseCase

data class RescueRecordUseCase(
    val addRescueRecordUseCase: AddRescueRecordUseCase,
    val getRescueRecordUseCase: GetRescueRecordUseCase,
    val getRideHistoryUseCase: GetRideHistoryUseCase,
    val rateRescueUseCase: RateRescueUseCase,
    val rateRescuerUseCase: RateRescuerUseCase,
    val updateStatsUseCase: UpdateStatsUseCase,
    val addRideMetricsUseCase: AddRideMetricsUseCase,
    val rideDetailsUseCase: RideDetailsUseCase,
    val rideMetricsUseCase: RideMetricsUseCase
)
