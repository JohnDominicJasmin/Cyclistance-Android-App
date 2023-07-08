package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event

sealed class EmergencyCallEvent {
    object ContactDeleteSuccess : EmergencyCallEvent()
    object ContactDeleteFailed : EmergencyCallEvent()
}