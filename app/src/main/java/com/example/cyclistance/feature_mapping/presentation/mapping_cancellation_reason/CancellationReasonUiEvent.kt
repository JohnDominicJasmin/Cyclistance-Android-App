package com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason

sealed class CancellationReasonUiEvent{
    data class ShowToastMessage(val message: String): CancellationReasonUiEvent()
    object ShowMappingScreen: CancellationReasonUiEvent()
}
