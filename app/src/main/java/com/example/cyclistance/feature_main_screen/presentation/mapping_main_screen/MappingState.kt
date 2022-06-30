package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import android.location.Address
import com.google.android.gms.maps.model.LatLng

data class MappingState(
    val isLoading: Boolean =  false,
    val findAssistanceButtonVisible: Boolean = true,
    val addresses: List<Address> = emptyList(),
    val currentLatLng: LatLng? = null
)