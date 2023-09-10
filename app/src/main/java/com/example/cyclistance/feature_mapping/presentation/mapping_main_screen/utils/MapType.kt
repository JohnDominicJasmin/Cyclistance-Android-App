package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

sealed class MapType(val type: String){
    data object Default: MapType(type = "Default")
    data object HazardousLane: MapType(type = "Hazardous Lane")
    data object Traffic: MapType(type = "Traffic")
}
