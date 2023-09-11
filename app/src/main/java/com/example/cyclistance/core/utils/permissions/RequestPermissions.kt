package com.example.cyclistance.core.utils.permissions

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
            if(!shouldShowRationale){
                onDenied()
            }else {
                launchMultiplePermissionRequest()
            }
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
           if(!status.shouldShowRationale){
               onDenied()
           }else{
               launchPermissionRequest()
           }
        }
        status.shouldShowRationale -> {
            onExplain()
        }
        else -> onDenied()

    }


}


