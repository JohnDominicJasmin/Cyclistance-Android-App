package com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AccessRequestHelpDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDeny: () -> Unit,
    onAllow: () -> Unit) {


    val isDarkTheme = IsDarkTheme.current
    val icon = remember(isDarkTheme) {
        if (isDarkTheme) R.drawable.ic_request_help_dark else R.drawable.ic_request_help_light
    }


    ProminentDialogCreator(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = "Access your Requesting Help",
        description = "Cyclistance collects your location data to enable the 'Request Help' feature, allowing others to see your location when you need assistance. This is crucial for ensuring your safety and the effectiveness of the service.",
        icon = icon,
        warningText = "Tap 'Allow' to grant access, as denying it may hinder the app's ability to provide timely help.",
        onDeny = onDeny, onAllow = onAllow)


}

@Preview
@Composable
fun PreviewAccessRequestHelpDialog() {
    CyclistanceTheme(darkTheme = false) {
        AccessRequestHelpDialog(
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