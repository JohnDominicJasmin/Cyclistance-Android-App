package com.example.cyclistance.feature_authentication.presentation.auth_reset_password.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.presentation.dialogs.alert_dialog.AlertDialog
import com.example.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.feature_authentication.presentation.auth_reset_password.event.ResetPasswordUiEvent
import com.example.cyclistance.feature_authentication.presentation.auth_reset_password.state.ResetPasswordState
import com.example.cyclistance.feature_authentication.presentation.auth_reset_password.state.ResetPasswordUiState
import com.example.cyclistance.feature_authentication.presentation.common.PasswordTextField
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun ResetPasswordContent(
    modifier: Modifier = Modifier,
    currentPassword: TextFieldValue,
    newPassword: TextFieldValue,
    confirmPassword: TextFieldValue,
    state: ResetPasswordState,
    uiState: ResetPasswordUiState,
    event: (ResetPasswordUiEvent) -> Unit
) {

    val isDarkTheme = IsDarkTheme.current

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        Box(
            modifier = Modifier

                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter) {


            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Image(
                    contentDescription = "Display Image",
                    painter = painterResource(id = if (isDarkTheme) R.drawable.ic_reset_pw_dark else R.drawable.ic_reset_pw_light)
                )

                Column(
                    modifier = Modifier
                        .padding(top = 25.dp)
                        .padding(horizontal = 15.dp)
                ) {

                    Text(
                        text = "Reset Password",
                        style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp))

                    Text(
                        text = "Create new password so you can login to your account",
                        style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Normal),
                        color = Black500)

                }


                Column(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {

                    PasswordTextField(
                        placeholderText = "Current Password",
                        enabled = !state.isLoading,
                        password = currentPassword,
                        hasTrailingIcon = false,
                        passwordExceptionMessage = uiState.currentPasswordErrorMessage,
                        onValueChange = { event(ResetPasswordUiEvent.OnChangeCurrentPassword(it)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                        ))


                    PasswordTextField(
                        placeholderText = "New Password",
                        enabled = !state.isLoading,
                        hasTrailingIcon = false,
                        password = newPassword,
                        passwordExceptionMessage = uiState.newPasswordErrorMessage,
                        onValueChange = { event(ResetPasswordUiEvent.OnChangeNewPassword(it)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                        ))



                    PasswordTextField(
                        enabled = !state.isLoading,
                        hasTrailingIcon = false,
                        placeholderText = "Confirm New Password",
                        password = confirmPassword,
                        passwordExceptionMessage = uiState.confirmPasswordErrorMessage,
                        onValueChange = { event(ResetPasswordUiEvent.OnChangeConfirmPassword(it)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { event(ResetPasswordUiEvent.KeyboardActionDone) }))

                }

                ButtonNavigation(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    negativeButtonEnabled = !state.isLoading,
                    positiveButtonEnabled = !state.isLoading,
                    positiveButtonText = "Update",
                    onClickNegativeButton = { event(ResetPasswordUiEvent.OnClickCancel) },
                    onClickPositiveButton = { event(ResetPasswordUiEvent.OnClickUpdate) })

            }


            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (uiState.alertDialogState.visible()) {
                AlertDialog(
                    modifier = Modifier.align(Alignment.Center),
                    alertDialog = uiState.alertDialogState,
                    onDismissRequest = { event(ResetPasswordUiEvent.DismissAlertDialog) })

            }

            if (uiState.isNoInternetVisible) {
                NoInternetDialog(modifier = Modifier.align(Alignment.Center), onDismiss = {
                    event(ResetPasswordUiEvent.DismissNoInternetDialog)
                })
            }

        }

    }


}

@Preview(device = "id:Galaxy Nexus")
@Composable
private fun PreviewResetPasswordContent1() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            ResetPasswordContent(
                modifier = Modifier,
                currentPassword = TextFieldValue(),
                newPassword = TextFieldValue(),
                confirmPassword = TextFieldValue(),
                state = ResetPasswordState(),
                uiState = ResetPasswordUiState(),
                event = {}
            )
        }
    }
}

@Preview(device = "id:Galaxy Nexus", name = "Is Loading shown")
@Composable
private fun PreviewResetPasswordContent2() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            ResetPasswordContent(
                modifier = Modifier,
                currentPassword = TextFieldValue("bla bla bla"),
                newPassword = TextFieldValue(),
                confirmPassword = TextFieldValue(),
                state = ResetPasswordState(isLoading = true),
                uiState = ResetPasswordUiState(),
                event = {}
            )
        }
    }
}


@Preview(device = "id:Galaxy Nexus", name = "Is Loading NoInternetDialog Shown")
@Composable
private fun PreviewResetPasswordContent3() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            ResetPasswordContent(
                modifier = Modifier,
                currentPassword = TextFieldValue("bla bla bla"),
                newPassword = TextFieldValue(),
                confirmPassword = TextFieldValue(),
                state = ResetPasswordState(isLoading = false),
                uiState = ResetPasswordUiState(
                    isNoInternetVisible = false,
                    alertDialogState = AlertDialogState(
                        title = "Title",
                        description = "aoisdnaoisdnaoinoaisn",
                        icon = R.raw.success)),
                event = {}
            )
        }
    }
}


