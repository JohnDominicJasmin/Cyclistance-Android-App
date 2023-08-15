package com.example.cyclistance.core.utils.permissions

import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
inline fun MultiplePermissionsState.requestPermission(
    onGranted: () -> Unit,
    onExplain: () -> Unit) {


    when{

        allPermissionsGranted -> {
            onGranted()
        }

        !allPermissionsGranted -> {
            if(!shouldShowRationale){
                onExplain()
            }else {
                launchMultiplePermissionRequest()
            }
        }

        shouldShowRationale -> {
            onExplain()
        }
    }


}


@OptIn(ExperimentalPermissionsApi::class)
inline fun PermissionState.requestPermission(
    onGranted: () -> Unit,
    onExplain: () -> Unit) {


    when{
        status.isGranted -> {
            onGranted()
        }
        !status.isGranted -> {
           if(!status.shouldShowRationale){
               onExplain()
           }else{
               launchPermissionRequest()
           }
        }
        status.shouldShowRationale -> {
            onExplain()
        }

    }


}


