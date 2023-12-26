package com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AccessLocationDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDeny: () -> Unit,
    onAllow: () -> Unit) {


    val isDarkTheme = IsDarkTheme.current
    val icon = remember(isDarkTheme) {
        if (isDarkTheme) R.drawable.ic_access_location_dark else R.drawable.ic_access_location_light
    }
    ProminentDialogCreator(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = "Access your Location",
        description = "Cyclistance App collects location information for accessing and sharing purposes, enabling you to share your current location with others even when the app is in the background or not actively in use.",
        icon = icon,
        warningText = "Tap 'Allow' to authorize access, as refusal may affect the app's capability to accurately display your location on the map.",
        onDeny = onDeny,
        onAllow = onAllow)
}

@Preview
@Composable
fun PreviewAccessLocationDialog() {
    CyclistanceTheme(darkTheme = false) {
        AccessLocationDialog(
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