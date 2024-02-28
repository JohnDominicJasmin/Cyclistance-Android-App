package com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AccessGalleryDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onDeny: () -> Unit,
    onAllow: () -> Unit) {

    val isDarkTheme = IsDarkTheme.current
    val icon = remember(isDarkTheme) {
        if(isDarkTheme) R.drawable.ic_request_help_dark else R.drawable.ic_request_help_light
    }

    ProminentDialogCreator(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = "Access your Gallery",
        description = "Cyclistance collects access to your gallery to enable photo sharing with others. Denying access may limit the app's functionality. ",
        icon = icon,
        warningText = "Tap 'Allow' to proceed for seamless photo sharing.",
        onDeny = onDeny,
        onAllow = onAllow)
}

@Preview
@Composable
fun PreviewAccessGalleryDialog() {
    CyclistanceTheme(darkTheme = false) {
        AccessGalleryDialog(
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