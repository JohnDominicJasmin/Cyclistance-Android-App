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
fun AccessForegroundLocationDialog(
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
        title = "Foreground Location Needed",
        description = "Cyclistance enhances your experience by using your current location for precise navigation and social connectivity. Grant foreground location access to enjoy seamless features.",
        icon = icon,
        warningText = "Tap 'Allow' to authorize access, as refusal may affect the app's capability to accurately display your location on the map.",
        onDeny = onDeny,
        onAllow = onAllow)
}

@Preview
@Composable
fun PreviewAccessLocationDialog() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            AccessForegroundLocationDialog(
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