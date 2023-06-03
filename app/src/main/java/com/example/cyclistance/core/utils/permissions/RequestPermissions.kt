
package com.example.cyclistance.core.utils.permissions

import android.content.Context
import android.widget.Toast
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
inline fun MultiplePermissionsState.requestPermission(
    context: Context,
    rationalMessage: String,
    onGranted: () -> Unit,
    onDenied: () -> Unit) {

    val isDenied = !allPermissionsGranted && !shouldShowRationale

    if (isDenied) {
        onDenied()
        return
    }

    if (!allPermissionsGranted) {
        this.launchMultiplePermissionRequest()
        return
    }

    if (shouldShowRationale) {
        Toast.makeText(
            context,
            rationalMessage,
            Toast.LENGTH_LONG).show()
        return
    }



    onGranted()
}

@OptIn(ExperimentalPermissionsApi::class)
inline fun PermissionState.requestPermission(
    context: Context,
    rationalMessage: String,
    onGranted: () -> Unit,
    onDenied: () -> Unit) {

    val isDenied = !status.isGranted && !status.shouldShowRationale

    if (isDenied) {
        onDenied()
        return
    }
    if (!status.isGranted) {
        launchPermissionRequest()
        return
    }
    if(status.shouldShowRationale){
        Toast.makeText(context, rationalMessage, Toast.LENGTH_LONG).show()
        return
    }



    onGranted()



}
