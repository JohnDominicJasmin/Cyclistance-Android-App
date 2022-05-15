package com.example.cyclistance.feature_main_screen.data.remote.dto


data class UserDto(
    val id:String = "",
    val name:String = "",
    val address:String = "",
    val location: Location? = null
)
