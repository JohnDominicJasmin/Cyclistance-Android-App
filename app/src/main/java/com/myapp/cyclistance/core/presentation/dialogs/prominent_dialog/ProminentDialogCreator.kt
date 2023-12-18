package com.myapp.cyclistance.core.presentation.dialogs.prominent_dialog

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.myapp.cyclistance.R
import com.myapp.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.myapp.cyclistance.theme.Black500
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun ProminentDialogCreator(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    @DrawableRes icon: Int,
    warningText: String,
    onDeny: () -> Unit,
    onAllow: () -> Unit) {


    val (isDialogOpen, onDialogVisibilityToggle) = rememberSaveable { mutableStateOf(true) }

    if (isDialogOpen) {

        Dialog(
            onDismissRequest = {
                onDismissRequest()
                onDialogVisibilityToggle(false)
            }, properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false)) {

            Surface(
                modifier = modifier.fillMaxSize(),
                color = MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.onBackground) {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally) {


                    Row {
                        IconButton(onClick = onDismissRequest, modifier = Modifier) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Dialog",
                                modifier = Modifier,
                            )
                        }

                        Spacer(modifier = Modifier.fillMaxWidth())

                    }

                    Text(
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        text = title,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .padding(horizontal = 20.dp),
                        textAlign = TextAlign.Start)

                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = "Prominent Dialog Icon Display",
                        modifier = Modifier
                            .padding(vertical = 24.dp)
                            .padding(horizontal = 20.dp),
                        contentScale = ContentScale.FillBounds
                    )


                    Text(
                        text = warningText,
                        style = MaterialTheme.typography.body2.copy(color = Black500),
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        textAlign = TextAlign.Center)

                    ButtonNavigation(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(0.8f),
                        negativeButtonEnabled = true,
                        positiveButtonEnabled = true,
                        negativeButtonText = "Deny",
                        positiveButtonText = "Allow",
                        onClickNegativeButton = {
                            onDeny()
                            onDialogVisibilityToggle(false)
                        },
                        onClickPositiveButton = {
                            onAllow()
                            onDialogVisibilityToggle(false)
                        })
                }

            }
        }

    }
}


@Preview
@Composable
fun PreviewProminentDialogCreatorDark() {
    CyclistanceTheme(darkTheme = true) {
        ProminentDialogCreator(
            onDismissRequest = {},
            title = "Access your Gallery",
            description = "Cyclistance gathers your location information for accessing and sharing purposes, enabling you to share your current location with others. This improves the app's features for navigation and social connectivity.",
            icon = R.drawable.ic_request_help_dark,
            warningText = "Tap 'Allow' to grant access, as denying it may hinder the app's ability to provide timely help.",
            onDeny = {},
            onAllow = {})
    }
}

@Preview
@Composable
fun PreviewProminentDialogCreatorLight() {
    CyclistanceTheme(darkTheme = false) {
        ProminentDialogCreator(
            onDismissRequest = {},
            title = "Access your Gallery",
            description = "Cyclistance gathers your location information for accessing and sharing purposes, enabling you to share your current location with others. This improves the app's features for navigation and social connectivity.",
            icon = R.drawable.ic_request_help_dark,
            warningText = "Tap 'Allow' to grant access, as denying it may hinder the app's ability to provide timely help.",
            onDeny = {},
            onAllow = {})
    }
}