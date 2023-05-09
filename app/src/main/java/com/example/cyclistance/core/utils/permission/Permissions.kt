
package com.example.cyclistance.core.utils.permission

import android.content.Context
import android.widget.Toast
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
inline fun MultiplePermissionsState.requestPermission(
    context: Context,
    rationalMessage: String,
    deniedMessage: String = "Permission denied. Please grant the necessary permissions to use this feature.",
    onGranted: () -> Unit) {

    val isDenied = !allPermissionsGranted && !shouldShowRationale

    if(isDenied){
        Toast.makeText(context, deniedMessage, Toast.LENGTH_LONG).show()
        return
    }

    if (!allPermissionsGranted) {
        launchMultiplePermissionRequest()
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
    deniedMessage: String = "Permission denied. Please grant the necessary permissions to use this feature.",
    onGranted: () -> Unit) {

    val isDenied = !status.isGranted && !status.shouldShowRationale

    if(isDenied){
        Toast.makeText(context, deniedMessage, Toast.LENGTH_LONG).show()
        return
    }
    if(!status.isGranted){
        launchPermissionRequest()
        return
    }
    if(status.shouldShowRationale){
        Toast.makeText(context, rationalMessage, Toast.LENGTH_LONG).show()
        return
    }



    onGranted()



}
