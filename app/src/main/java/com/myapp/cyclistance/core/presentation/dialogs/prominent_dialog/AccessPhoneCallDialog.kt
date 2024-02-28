package com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AccessPhoneCallDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDeny: () -> Unit,
    onAllow: () -> Unit) {

    val isDarkTheme = IsDarkTheme.current
    val icon = remember(isDarkTheme) {
        if (isDarkTheme) R.drawable.ic_access_phone_call_dark else R.drawable.ic_access_phone_call_light
    }


    ProminentDialogCreator(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = "Access your Phone Call",
        description = "Cyclistance collects access to phone call data to reach emergency contacts swiftly in critical situations.",
        icon = icon,
        warningText = "Tap 'Allow' to ensure proper functionality for emergency calls.",
        onDeny = onDeny,
        onAllow = onAllow)

}

@Preview
@Composable
fun PreviewAccessPhoneCallDialog() {
    CyclistanceTheme(darkTheme = false) {
        AccessPhoneCallDialog(
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