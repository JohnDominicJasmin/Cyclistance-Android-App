package com.example.cyclistance.feature_rescue_record.presentation.rescue_details.event

import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideSummary

sealed class RescueDetailsEvent{
    data class GetRescueRecordSuccess(val rideSummary: RideSummary): RescueDetailsEvent()
    data class GetRescueRecordFailed(val message: String): RescueDetailsEvent()
}
