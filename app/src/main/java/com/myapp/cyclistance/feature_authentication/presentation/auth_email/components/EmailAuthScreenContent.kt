package com.myapp.cyclistance.feature_authentication.presentation.auth_email.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.presentation.dialogs.alert_dialog.AlertDialog
import com.myapp.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.myapp.cyclistance.feature_authentication.presentation.auth_email.event.EmailAuthUiEvent
import com.myapp.cyclistance.feature_authentication.presentation.auth_email.state.EmailAuthState
import com.myapp.cyclistance.feature_authentication.presentation.auth_email.state.EmailAuthUiState
import com.myapp.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.DIALOG_ID
import com.myapp.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.ICON_DISPLAY_ID
import com.myapp.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.PROGRESS_BAR_ID
import com.myapp.cyclistance.feature_authentication.presentation.common.visible
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme


@Preview(device = "id:pixel_xl")
@Composable
fun PreviewEmailAuthScreenDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(true) {
            EmailAuthScreenContent(
                emailAuthState = EmailAuthState(
                    savedAccountEmail = "johndoe@gmail.com",
                    secondsLeft = 10,
                ),
            )
        }
    } 
}
@Preview(device = "id:pixel_xl")
@Composable
fun PreviewEmailAuthScreenLight() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(false) {
            EmailAuthScreenContent(
                emailAuthState = EmailAuthState(
                    savedAccountEmail = "johndoe@gmail.com",
                    secondsLeft = 10,
                ),
            )
        }
    }
}


@Composable
fun EmailAuthScreenContent(
    modifier: Modifier = Modifier,
    emailAuthState: EmailAuthState = EmailAuthState(),
    uiState: EmailAuthUiState = EmailAuthUiState(),
    event: (EmailAuthUiEvent) -> Unit = {}) {

    val isDarkTheme = IsDarkTheme.current

    Surface(
        color = MaterialTheme.colors.background,
        modifier = modifier.fillMaxSize()
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            ConstraintLayout(
                constraintSet = emailAuthConstraints,
                modifier = Modifier
                    .fillMaxHeight(0.9f)
                    .fillMaxWidth()) {

                Image(
                    contentDescription = "App Icon",
                    painter = painterResource(id = if (isDarkTheme) R.drawable.ic_dark_email else R.drawable.ic_light_email),
                    modifier = Modifier
                        .layoutId(ICON_DISPLAY_ID),
                    contentScale = ContentScale.FillBounds
                )

                EmailAuthTextStatus(email = emailAuthState.savedAccountEmail)


                if (uiState.alertDialogState.visible()) {
                    AlertDialog(
                        alertDialog = uiState.alertDialogState,
                        onDismissRequest = {
                            event(EmailAuthUiEvent.DismissAlertDialog)
                        }
                    )
                }

                if (emailAuthState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.layoutId(
                            PROGRESS_BAR_ID))
                }


                val secondsRemainingText =
                    remember(uiState.isTimerRunning, emailAuthState.secondsLeft) {
                        if (uiState.isTimerRunning) "Resend Email in ${emailAuthState.secondsLeft}s" else "Resend Email"
                    }

                EmailAuthVerifyEmailButton(
                    onClickVerifyButton = {
                        event(EmailAuthUiEvent.VerifyEmailAuth)
                    },
                    enabled = !emailAuthState.isLoading)

                EmailAuthResendButton(
                    text = secondsRemainingText,
                    isEnabled = !uiState.isTimerRunning && !emailAuthState.isLoading,
                    onClickResendButton = {
                        event(EmailAuthUiEvent.ResendEmailAuth)
                    })

                if (uiState.isNoInternetVisible) {
                    NoInternetDialog(
                        onDismiss = {
                            event(EmailAuthUiEvent.DismissNoInternetDialog)
                        },
                        modifier = Modifier.layoutId(DIALOG_ID),
                    )
                }
            }

        }
    }
}
