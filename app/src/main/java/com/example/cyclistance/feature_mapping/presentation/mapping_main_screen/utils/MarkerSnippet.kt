package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

sealed class MarkerSnippet(val type: String) {
    object NearbyCyclistSnippet: MarkerSnippet(type = "nearby_cyclist")
    object HazardousLaneSnippet: MarkerSnippet(type = "hazardous_lane_snippet")

}