package com.myapp.cyclistance.feature_rescue_record.presentation.history_details.event

sealed class HistoryDetailsUiEvent{
    data object CloseHistoryDetails: HistoryDetailsUiEvent()
}
