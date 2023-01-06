package com.example.cyclistance.feature_mapping.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@ExperimentalPermissionsApi
@Composable
fun RequestMultiplePermissions(
    multiplePermissionsState: MultiplePermissionsState,
    deniedMessage: String = "Give this app a permission to proceed.",
    rationaleMessage: String = "To use this app's functionalities, you need to give us the permission.",
    onPermissionGranted:  () -> Unit = {}
) {

    HandleRequests(
        multiplePermissionsState = multiplePermissionsState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                deniedMessage = deniedMessage,
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = {
                    multiplePermissionsState.launchMultiplePermissionRequest()
                }
            )
        },
        onPermissionGranted = onPermissionGranted
    )
}

@ExperimentalPermissionsApi
@Composable
private fun HandleRequests(
    multiplePermissionsState: MultiplePermissionsState,
    deniedContent: @Composable (Boolean) -> Unit,
    onPermissionGranted: () -> Unit
) {
    var shouldShowRationale by remember { mutableStateOf(false) }
    val result = remember(multiplePermissionsState.permissions) {
        multiplePermissionsState.permissions.all {
            shouldShowRationale = it.status.shouldShowRationale
            it.status == PermissionStatus.Granted
        }
    }
    if (result) {
        onPermissionGranted()
    } else {
        deniedContent(shouldShowRationale)
    }
}