package com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AccessCameraDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDeny: () -> Unit,
    onAllow: () -> Unit) {

    val isDarkTheme = IsDarkTheme.current
    val icon = remember(isDarkTheme) {
        if (isDarkTheme) R.drawable.ic_access_camera_dark else R.drawable.ic_access_camera_light
    }

    ProminentDialogCreator(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = "Access your Camera",
        description = "Cyclistance requests access to your camera to capture photos and share them with others for seamless communication. ",
        icon = icon,
        warningText = "Tap 'Allow' to enable this feature, as denying access may disrupt the app's functionality.",
        onDeny = onDeny,
        onAllow = onAllow)

}

@Preview
@Composable
fun PreviewAccessCameraDialog() {
    CyclistanceTheme(darkTheme = false) {
        AccessCameraDialog(
            modifier = Modifier,
            onDismissRequest = {

            },
            onDeny = {

            },
            onAllow = {

            }
        )
    }
}