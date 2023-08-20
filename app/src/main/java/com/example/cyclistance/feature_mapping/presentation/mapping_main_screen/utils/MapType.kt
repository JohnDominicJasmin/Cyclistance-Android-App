package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

sealed class MapType(val type: String){
    object Default: MapType(type = "Default")
    object HazardousLane: MapType(type = "Hazardous Lane")
}
