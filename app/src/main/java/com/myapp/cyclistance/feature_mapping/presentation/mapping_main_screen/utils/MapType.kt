package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.utils

sealed class MapType(val type: String){
    data object NearbyCyclists: MapType(type = "Nearby Cyclists")
    data object HazardousLane: MapType(type = "Hazardous Lane")
    data object Traffic: MapType(type = "Traffic")
}
