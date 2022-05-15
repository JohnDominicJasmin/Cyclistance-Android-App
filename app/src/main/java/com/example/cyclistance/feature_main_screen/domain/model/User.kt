package com.example.cyclistance.feature_main_screen.domain.model

import com.example.cyclistance.feature_main_screen.data.remote.dto.Location

data class User(
    val id:String = "",
    val name:String = "",
    val address:String = "",
    val location: Location? = null
)
