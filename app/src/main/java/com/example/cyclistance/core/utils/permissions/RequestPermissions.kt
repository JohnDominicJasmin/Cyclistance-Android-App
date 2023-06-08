package com.example.cyclistance.core.utils.permissions

import android.os.Build
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
inline fun MultiplePermissionsState.requestPermission(
    onGranted: () -> Unit,
    onExplain: () -> Unit) {

    val version = Build.VERSION.SDK_INT
    val android30 = Build.VERSION_CODES.R

    when {
        allPermissionsGranted -> onGranted()
        shouldShowRationale -> launchMultiplePermissionRequest()
        version >= android30 && !shouldShowRationale -> onExplain()
        else -> onExplain()

    }
}


@OptIn(ExperimentalPermissionsApi::class)
inline fun PermissionState.requestPermission(
    onGranted: () -> Unit,
    onExplain: () -> Unit) {


    val version = Build.VERSION.SDK_INT
    val android30 = Build.VERSION_CODES.R


    when {
        status.isGranted -> onGranted()
        status.shouldShowRationale -> launchPermissionRequest()
        (version >= android30 && !status.shouldShowRationale) -> onExplain()
        else -> onExplain()
    }

}


