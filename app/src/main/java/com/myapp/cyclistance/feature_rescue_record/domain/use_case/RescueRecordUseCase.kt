package com.myapp.cyclistance.feature_rescue_record.domain.use_case

data class RescueRecordUseCase(
    val addRescueRecordUseCase: AddRescueRecordUseCase,
    val getRescueRecordUseCase: GetRescueRecordUseCase,
    val getRideHistoryUseCase: GetRideHistoryUseCase,
    val rateRescueUseCase: RateRescueUseCase,
    val rateRescuerUseCase: RateRescuerUseCase,
    val updateStatsUseCase: UpdateStatsUseCase,
    val addRideMetricsUseCase: AddRideMetricsUseCase,
)
