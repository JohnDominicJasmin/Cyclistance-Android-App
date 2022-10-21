
package com.example.cyclistance.core.utils.permission

import android.content.Context
import android.widget.Toast
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
inline fun MultiplePermissionsState.requestPermission(
    context: Context,
    rationalMessage: String,
    onGranted: () -> Unit) {

    if (allPermissionsGranted) {
        onGranted()
        return
    }

    if (!shouldShowRationale) {
        Toast.makeText(
            context,
            rationalMessage,
            Toast.LENGTH_SHORT).show()
        return
    }
    launchMultiplePermissionRequest()
}

@OptIn(ExperimentalPermissionsApi::class)
inline fun PermissionState.requestPermission(
    context: Context,
    rationalMessage: String,
    onGranted: () -> Unit) {

    if(status.isGranted){
        onGranted()
        return
    }
    if(!status.shouldShowRationale){
        Toast.makeText(
            context,
            rationalMessage,
            Toast.LENGTH_SHORT).show()
        return
    }
    launchPermissionRequest()

}
