package com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen

import android.location.Address

sealed class MappingEvent {
    data class UploadProfile(val addresses: List<Address>) : MappingEvent()
}
