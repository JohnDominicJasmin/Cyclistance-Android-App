package com.example.cyclistance.core.utils.logger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.example.cyclistance.BuildConfig
import timber.log.Timber

class Ref(var value: Int)

@Composable
inline fun LogCompositions(msg: String) {
    if (BuildConfig.DEBUG) {
        val ref = remember { Ref(0) }
        SideEffect { ref.value++ }
        Timber.d( "Compositions: $msg ${ref.value}")
    }
}