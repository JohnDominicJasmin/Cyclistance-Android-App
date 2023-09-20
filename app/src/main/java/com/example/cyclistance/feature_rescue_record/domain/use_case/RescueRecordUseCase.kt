package com.example.cyclistance.feature_rescue_record.domain.use_case

data class RescueRecordUseCase(
    val addRescueRecordUseCase: AddRescueRecordUseCase,
    val rescueDetailsUseCase: RescueDetailsUseCase,
    val getRescueRecordUseCase: GetRescueRecordUseCase
)
