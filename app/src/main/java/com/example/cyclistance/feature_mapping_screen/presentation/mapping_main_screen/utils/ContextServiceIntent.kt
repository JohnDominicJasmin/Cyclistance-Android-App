package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils

import android.content.Context
import android.content.Intent
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.service.LocationService

fun Context.startLocationServiceIntentAction(intentAction:String = MappingConstants.ACTION_START){
    Intent(this, LocationService::class.java).apply {
        action = intentAction
        startService(this)
    }
}