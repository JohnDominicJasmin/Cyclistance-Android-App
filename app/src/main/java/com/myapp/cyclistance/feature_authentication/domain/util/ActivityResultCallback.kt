package com.myapp.cyclistance.feature_authentication.domain.util

import android.content.Intent
import androidx.compose.runtime.staticCompositionLocalOf

val LocalActivityResultCallbackManager =
    staticCompositionLocalOf<ActivityResultCallbackManager> { error("No ActivityResultCallbackManager provided") }

interface ActivityResultCallbackI {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
}