package com.example.cyclistance.feature_rescue_record.presentation.rescue_results

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MappingConstants.BROKEN_CHAIN_TEXT
import com.example.cyclistance.core.utils.constants.MappingConstants.BROKEN_FRAME_TEXT
import com.example.cyclistance.core.utils.constants.MappingConstants.FAULTY_BRAKES_TEXT
import com.example.cyclistance.core.utils.constants.MappingConstants.FLAT_TIRES_TEXT
import com.example.cyclistance.core.utils.constants.MappingConstants.INCIDENT_TEXT
import com.example.cyclistance.core.utils.constants.MappingConstants.INJURY_TEXT
import com.example.cyclistance.core.utils.constants.RescueRecordConstants.RESCUE_RESULT_VM_STATE_KEY
import com.example.cyclistance.feature_rescue_record.domain.model.ui.RideDetails
import com.example.cyclistance.feature_rescue_record.domain.use_case.RescueRecordUseCase
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.event.RescueResultVmEvent
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.state.RescueResultState
import com.example.cyclistance.feature_user_profile.domain.model.UserStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
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
            is RescueResultVmEvent.RateRescue -> {
                rateRescue(event.rating)
            }

            RescueResultVmEvent.UpdateUserStats -> {
                updateUserStats()
            }
        }
        saveState()
    }

    private fun updateUserStats() {
        viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
            runCatching {
                val rideDetails = state.value.rideDetails
                val summary = rideDetails.rideSummary
                val averageSpeed = summary.averageSpeed
                rescueRecordUseCase.updateStatsUseCase(userStats = UserStats(
                    rescuerId = rideDetails.rescuerId,
                    rescueeId = rideDetails.rescueeId,
                    rescueOverallDistanceInMeters = summary.distance,
                    rescueAverageSpeed = averageSpeed,
                    rescueDescription = toRescueDescription(summary.iconDescription) ?: ""
                ))
            }.onFailure {
                Timber.e( "Failed to update user stats ${it.message}")
            }
        }
    }

    private fun toRescueDescription(description: String): String? {
        return when (description) {
            INJURY_TEXT -> "injuryCount"
            BROKEN_FRAME_TEXT -> "frameSnapCount"
            INCIDENT_TEXT -> "incidentCount"
            BROKEN_CHAIN_TEXT -> "brokenChainCount"
            FLAT_TIRES_TEXT -> "flatTireCount"
            FAULTY_BRAKES_TEXT -> "faultyBrakesCount"
            else -> null
        }
    }

    private fun rateRescue(rating: Float) {
        val validRating = rating.coerceIn(1f, 5f)
        val rideDetails = state.value.rideDetails

        viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
            runCatching {
                val rateRescue = rateRescueUseCase(rideDetails, validRating)
                val rateRescuer = rateRescuerUseCase(rideDetails, validRating)

                awaitAll(rateRescue, rateRescuer)
            }.onSuccess {
                _eventFlow.emit(value = RescueResultEvent.RatingSuccess)
            }.onFailure {
                _eventFlow.emit(value = RescueResultEvent.RatingFailed(message = it.message!!))
            }
            saveState()
        }
    }

    private suspend fun rateRescueUseCase(rideDetails: RideDetails, rating: Float) =
        coroutineScope {
            async {
                rescueRecordUseCase.rateRescueUseCase(
                    rescueId = rideDetails.rideId,
                    rating = rating.toDouble(),
                    ratingText = ratingToDescription(rating)
                )
            }
        }

    private suspend fun rateRescuerUseCase(rideDetails: RideDetails, rating: Float) =
        coroutineScope {
            async {
                rescueRecordUseCase.rateRescuerUseCase(
                    rescuerId = rideDetails.rescuerId,
                    rating = rating.toDouble()
                )
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