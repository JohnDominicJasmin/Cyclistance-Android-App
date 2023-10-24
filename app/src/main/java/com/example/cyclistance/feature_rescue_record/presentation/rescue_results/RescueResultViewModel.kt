package com.example.cyclistance.feature_rescue_record.presentation.rescue_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_RESULT_VM_STATE_KEY
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultVmEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.state.RescueResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RescueResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val rescueRecordUseCase: RescueRecordUseCase

): ViewModel() {

    private val _state = MutableStateFlow(
        savedStateHandle[RESCUE_RESULT_VM_STATE_KEY] ?: RescueResultState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RescueResultEvent>()
    val eventFlow = _eventFlow.asSharedFlow()



    init {
        loadRideDetails()
    }

    private fun loadRideDetails(){
        viewModelScope.launch {
            rescueRecordUseCase.rescueDetailsUseCase().collect{ rideDetails ->
                _state.update { it.copy(rideDetails = rideDetails) }
            }
            saveState()
        }
    }

    private fun saveState(){
        savedStateHandle[RESCUE_RESULT_VM_STATE_KEY] = state.value
    }
    fun onEvent(event: RescueResultVmEvent){
        when(event){
            is RescueResultVmEvent.RateRescuer -> {
                rateRescuer(event.rating)
            }
        }
        saveState()
    }

    private fun rateRescuer(rating: Float) {
        viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
            runCatching {
                val validRating = rating.coerceIn(1f, 5f)

                val rideDetails = state.value.rideDetails

                rescueRecordUseCase.rateRescueUseCase(
                    rescueId = rideDetails.rideId,
                    rating = validRating.toDouble(),
                    ratingText = ratingToDescription(validRating)
                )
            }.onSuccess {
                _eventFlow.emit(value = RescueResultEvent.RatingSuccess)
            }.onFailure {
                _eventFlow.emit(value = RescueResultEvent.RatingFailed(message = it.message!!))
            }

        }
    }
    private fun ratingToDescription(rating: Float): String {
        return when (rating) {
            in 0.0f..1.0f -> "Poor"
            in 1.1f..2.0f -> "Bad"
            in 2.1f..3.0f -> "Fair"
            in 3.1f..4.0f -> "Good"
            in 4.1f..5.0f -> "Excellent"
            else -> "Good"
        }
    }

}