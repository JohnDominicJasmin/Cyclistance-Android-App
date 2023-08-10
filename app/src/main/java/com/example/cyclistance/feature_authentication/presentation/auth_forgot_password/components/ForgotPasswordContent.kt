package com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.presentation.dialogs.alert_dialog.AlertDialog
import com.example.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.event.ForgotPasswordUiEvent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.state.ForgotPasswordState
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.state.ForgotPasswordUiState
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.BOTTOM_WAVE_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.BUTTONS_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.DIALOG_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.ICON_DISPLAY_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.PROGRESS_BAR_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TEXT_FIELDS_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TEXT_LABEL
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TOP_SPACER_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TOP_WAVE_ID
import com.example.cyclistance.feature_authentication.presentation.common.EmailTextField
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun ForgotPasswordContent(
    modifier: Modifier = Modifier,
    email: TextFieldValue,
    state: ForgotPasswordState,
    uiState: ForgotPasswordUiState,
    event: (ForgotPasswordUiEvent) -> Unit
) {

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background) {


        val isDarkTheme = IsDarkTheme.current

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            constraintSet = forgotPasswordConstraints) {


            Spacer(modifier = Modifier.layoutId(TOP_SPACER_ID))

            Image(
                contentDescription = "Display Image",
                painter = painterResource(id = if (isDarkTheme) R.drawable.ic_forgot_pw_dark else R.drawable.ic_forgot_pw_light),
                modifier = Modifier.layoutId(ICON_DISPLAY_ID),
            )

            Waves(
                topWaveLayoutId = TOP_WAVE_ID,
                bottomWaveLayoutId = BOTTOM_WAVE_ID)

            Column(
                modifier = Modifier
                    .layoutId(TEXT_LABEL)
                    .padding(top = 15.dp)
                    .padding(horizontal = 15.dp)) {
                Text(
                    text = "Forgot Password",
                    style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp))

                Text(
                    text = "Don’t worry it happens. Kindly enter the email address associated with your account.",
                    style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Normal),
                    color = Black500)

            }

            EmailTextField(
                modifier = Modifier
                    .layoutId(TEXT_FIELDS_ID)
                    .padding(horizontal = 15.dp),
                enabled = !state.isLoading,
                email = email,
                emailErrorMessage = uiState.emailErrorMessage,
                clearIconOnClick = { event(ForgotPasswordUiEvent.ClearEmailInput) },
                onValueChange = { event(ForgotPasswordUiEvent.OnChangeEmail(it)) },
                keyboardActions = KeyboardActions(onDone = {
                    event(ForgotPasswordUiEvent.KeyboardActionDone)
                }),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None)
                )


            ButtonNavigation(
                modifier = Modifier.layoutId(BUTTONS_ID),
                negativeButtonEnabled = !state.isLoading,
                positiveButtonEnabled = !state.isLoading,
                negativeButtonText = "Cancel",
                positiveButtonText = "Submit",
                onClickNegativeButton = { event(ForgotPasswordUiEvent.OnClickCancel) },
                onClickPositiveButton = { event(ForgotPasswordUiEvent.OnClickSubmit) }
            )


            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(PROGRESS_BAR_ID)
                )
            }

            if (uiState.alertDialogState.visible()) {
                AlertDialog(
                    modifier = Modifier.layoutId(DIALOG_ID),
                    alertDialog = uiState.alertDialogState, onDismissRequest = {
                        event(ForgotPasswordUiEvent.DismissAlertDialog)
                    })
            }

            if (uiState.isNoInternetVisible) {
                NoInternetDialog(
                    modifier = Modifier.layoutId(DIALOG_ID),
                    onDismiss = { event(ForgotPasswordUiEvent.DismissNoInternetDialog) }
                )
            }


        }

    }
}


@Preview(device = "id:Galaxy Nexus", name = "Dark Theme and Loading is showing")
@Composable
private fun PreviewForgotPasswordContentDark1() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            ForgotPasswordContent(
                state = ForgotPasswordState(isLoading = true),
                uiState = ForgotPasswordUiState(),
                event = {},
                email = TextFieldValue("aisndoaisndoian"))
        }
    }
}

@Preview(device = "id:Galaxy Nexus", name = "Dark Theme and Loading is not shown")
@Composable
private fun PreviewForgotPasswordContentDark2() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            ForgotPasswordContent(
                state = ForgotPasswordState(isLoading = false),
                uiState = ForgotPasswordUiState(),
                event = {},
                email = TextFieldValue("aisndoaisndoian"))
        }
    }
}

@Preview(device = "id:Galaxy Nexus", name = "Dark Theme and Loading is not shown")
@Composable
private fun PreviewForgotPasswordContentDark3() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            val uiState by rememberSaveable {
                mutableStateOf(ForgotPasswordUiState())
            }
            var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue())
            }

            val clearEmail = remember {
                {
                    email = TextFieldValue()
                }
            }

            val onChangeEmail = remember {
                { input: TextFieldValue ->
                    email = input
                }
            }

            val onClickCancel = remember {
                {

                }
            }


            ForgotPasswordContent(state = ForgotPasswordState(), modifier = Modifier.padding(),
                email = email,
                uiState = uiState,
                event = { event ->
                    when (event) {
                        ForgotPasswordUiEvent.ClearEmailInput -> clearEmail()
                        is ForgotPasswordUiEvent.OnChangeEmail -> onChangeEmail(event.email)
                        ForgotPasswordUiEvent.OnClickCancel -> onClickCancel()
                        ForgotPasswordUiEvent.OnClickSubmit -> TODO()
                        ForgotPasswordUiEvent.DismissAlertDialog -> TODO()
                    }
                }
            )
        }
    }
}


@Preview(device = "id:Galaxy Nexus", name = "Dark Theme Alert dialog is shown")
@Composable
private fun PreviewShowingAlertDialog() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            ForgotPasswordContent(
                state = ForgotPasswordState(isLoading = true),
                uiState = ForgotPasswordUiState(
                    alertDialogState = AlertDialogState(
                        title = "Successfully sent",
                        description = "Please check your email for the reset password link",
                        icon = R.raw.success
                    )
                ),
                event = {},
                email = TextFieldValue("aisndoaisndoian"))
        }
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
private fun PreviewForgotPasswordContentLight() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            ForgotPasswordContent(
                state = ForgotPasswordState(),
                uiState = ForgotPasswordUiState(),
                event = {},
                email = TextFieldValue("aisndoaisndoian"))
        }
    }
}


