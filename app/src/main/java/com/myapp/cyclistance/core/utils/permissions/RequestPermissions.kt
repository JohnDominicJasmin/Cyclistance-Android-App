package com.myapp.cyclistance.core.utils.permissions

import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
inline fun MultiplePermissionsState.requestPermission(
    onGranted: () -> Unit,
    onExplain: () -> Unit,
    onDenied: () -> Unit) {


    when{

        allPermissionsGranted -> {
            onGranted()
        }

        !allPermissionsGranted -> {
            launchMultiplePermissionRequest()
        }

        shouldShowRationale -> {
            onExplain()
        }

        else -> onDenied()
    }


}


@OptIn(ExperimentalPermissionsApi::class)
inline fun PermissionState.requestPermission(
    onGranted: () -> Unit,
    onExplain: () -> Unit,
    onDenied: () -> Unit) {


    when{
        status.isGranted -> {
            onGranted()
        }
        !status.isGranted -> {
            launchPermissionRequest()
        }
        status.shouldShowRationale -> {
            onExplain()
        }
        else -> onDenied()

    }


}


