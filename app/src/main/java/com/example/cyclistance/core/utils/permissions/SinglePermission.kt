package com.example.cyclistance.core.utils.permissions

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.permissions.*

@ExperimentalPermissionsApi
@Composable
fun RequestSinglePermission(
    permissionState: PermissionState,
    deniedMessage: String = "Give this app a permission to proceed.",
    rationaleMessage: String = "To use this app's functionalities, you need to give us the permission.",
    onPermissionGranted:  () -> Unit
) {


    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                deniedMessage = deniedMessage,
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = {
                    permissionState.launchPermissionRequest()
                }
            )
        },
        onPermissionGranted = onPermissionGranted
    )
}

@ExperimentalPermissionsApi
@Composable
private fun HandleRequest(
    permissionState: PermissionState,
    deniedContent: @Composable (Boolean) -> Unit,
    onPermissionGranted: () -> Unit
) {

    when (permissionState.status) {
        is PermissionStatus.Granted -> {
            onPermissionGranted()
        }
        is PermissionStatus.Denied -> {
         deniedContent(permissionState.status.shouldShowRationale)
        }
    }
}



@ExperimentalPermissionsApi
@Composable
fun PermissionDeniedContent(
    deniedMessage: String,
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {


    if (shouldShowRationale) {

        CustomAlertDialog(
            description = rationaleMessage,
            title = "Permission Request",
            buttonText = "Give Permission",
            onClickButton = onRequestPermission)
    }else {

        CustomAlertDialog(
            description = deniedMessage,
            title = "Permission is Required",
            buttonText = "Ok",
            onClickButton = onRequestPermission)
    }
}
@Composable
fun CustomAlertDialog(
    description: String,
    title:String,
    buttonText:String,
    onClickButton: () -> Unit
) {
    var alertDialogState by remember { mutableStateOf(true) }

    if (alertDialogState) {
        AlertDialog(
            onDismissRequest = { alertDialogState = false },
            title = {
                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(description)
            },
            confirmButton = {
                Button(onClick = {
                    onClickButton();
                    alertDialogState = false
                }) {
                    Text(buttonText)
                }
            }
        )
    }
}