package com.example.cyclistance.feature_authentication.domain.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent

class ActivityResultCallbackManager {
    private val listeners = mutableListOf<ActivityResultCallbackI>()

    fun addListener(listener : ActivityResultCallbackI) {
        listeners.add(listener)
    }

    fun removeListener(listener : ActivityResultCallbackI) {
        listeners.remove(listener)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) : Boolean =
        listeners.any { it.onActivityResult(requestCode, resultCode, data) }
}
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}