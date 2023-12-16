package com.myapp.cyclistance.core.utils.permissions

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
inline fun MultiplePermissionsState.requestPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit) {


    when{
        allPermissionsGranted -> onGranted()
        shouldShowRationale -> launchMultiplePermissionRequest()
        isPermanentlyDenied() -> onDenied()
        else -> launchMultiplePermissionRequest()


    }


}


@OptIn(ExperimentalPermissionsApi::class)
inline fun PermissionState.requestPermission(
    onGranted: () -> Unit,
    onDenied: () -> Unit) {


    when{

        hasPermission -> onGranted()
        shouldShowRationale -> launchPermissionRequest()
        isPermanentlyDenied() -> onDenied()
        else -> launchPermissionRequest()

    }


}

@ExperimentalPermissionsApi
fun MultiplePermissionsState.isGranted():Boolean{
    return allPermissionsGranted && permissionRequested
}

@ExperimentalPermissionsApi
fun PermissionState.isGranted():Boolean{
    return hasPermission && permissionRequested
}


@ExperimentalPermissionsApi
fun MultiplePermissionsState.isPermanentlyDenied(): Boolean {
    return !shouldShowRationale && !allPermissionsGranted && permissionRequested
}


@ExperimentalPermissionsApi
fun PermissionState.isPermanentlyDenied(): Boolean {
    return !shouldShowRationale && !hasPermission && permissionRequested
}

