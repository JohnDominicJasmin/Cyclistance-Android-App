package com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AccessBackgroundLocationDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDeny: () -> Unit,
    onAllow: () -> Unit) {
    val isDarkTheme = IsDarkTheme.current
    val icon = remember(isDarkTheme) {
        if (isDarkTheme) R.drawable.ic_prom_bg_location_dark else R.drawable.ic_prom_bg_location_light
    }


    ProminentDialogCreator(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = "Background Location Needed",
        description = "Cyclistance collects location data to enable “Request help”, “Respond to help”, & “Accepting help request” even when the app is closed or not in use.",
        icon = icon,
        warningText = "Tap 'Allow all the time' to authorize access, as declining may impact the app's ability to track progress when it's in the background.",
        onDeny = onDeny,
        onAllow = onAllow)
}


@Preview
@Composable
fun PreviewAccessBackgroundLocationDialog() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            AccessBackgroundLocationDialog(
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
}