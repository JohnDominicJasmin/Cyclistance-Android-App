package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.core.presentation.dialogs.alert_dialog.AlertDialog
import com.example.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event.SignUpUiEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.state.SignUpState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.state.SignUpUiState
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.theme.CyclistanceTheme

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewSignUpScreenDark() {
    CyclistanceTheme(true) {
        SignUpScreenContent(
            signUpState = SignUpState(),
            email = TextFieldValue(""),
            password = TextFieldValue(""),
            confirmPassword = TextFieldValue(""))
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewSignUpScreenLight() {
    CyclistanceTheme(false) {
        SignUpScreenContent(
            signUpState = SignUpState(),
            email = TextFieldValue(""),
            password = TextFieldValue(""),
            confirmPassword = TextFieldValue(""))
    }

}


@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    signUpState: SignUpState = SignUpState(),
    focusRequester: FocusRequester = FocusRequester(),
    uiState: SignUpUiState = SignUpUiState(),
    email: TextFieldValue,
    password: TextFieldValue,
    confirmPassword: TextFieldValue,
    event: (SignUpUiEvent) -> Unit = {},
) {

    Surface(color = MaterialTheme.colors.background, modifier = modifier.fillMaxSize()) {

        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
            constraintSet = signUpConstraints) {

            Spacer(
                modifier = Modifier.layoutId(
                    AuthenticationConstraintsItem.TopSpacer.layoutId
                ))

            Image(
                contentDescription = "App Icon",
                painter = painterResource(R.drawable.ic_app_icon_cyclistance),
                modifier = Modifier
                    .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId),
            )

            SignUpTextArea()


            Waves(
                topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId)



            if (uiState.alertDialogState.visible()) {
                AlertDialog(
                    alertDialog = uiState.alertDialogState,
                    onDismissRequest = {
                        event(SignUpUiEvent.DismissAlertDialog)
                    })
            }

            SignUpTextFieldsArea(
                focusRequester = focusRequester,
                state = signUpState,
                keyboardActionOnDone = {
                    event(SignUpUiEvent.KeyboardActionDone)
                },
                onValueChangeEmail = {
                    event(SignUpUiEvent.OnChangeEmail(it))
                },
                onValueChangePassword = {
                    event(SignUpUiEvent.OnChangePassword(it))
                },
                onValueChangeConfirmPassword = {
                    event(SignUpUiEvent.OnChangeConfirmPassword(it))
                },
                onClickPasswordVisibility = {
                    event(SignUpUiEvent.TogglePasswordVisibility)
                },
                email = email,
                emailErrorMessage = uiState.emailErrorMessage,
                password = password,
                passwordErrorMessage = uiState.passwordErrorMessage,
                confirmPassword = confirmPassword,
                confirmPasswordErrorMessage = uiState.confirmPasswordErrorMessage,
                passwordVisibility = uiState.passwordVisible
            )


            SignUpButton(enabled = !signUpState.isLoading, onClickSignUpButton = {
                event(SignUpUiEvent.SignUpWithEmailAndPassword)
            })
            SignUpClickableText(enabled = !signUpState.isLoading, onSignUpTextClick = {
                event(SignUpUiEvent.NavigateToSignIn)
            })


            if (signUpState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId))
            }

            if (uiState.isNoInternetVisible) {
                NoInternetDialog(
                    onDismiss = {
                        event(SignUpUiEvent.DismissNoInternetDialog)
                    },
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId),
                )
            }

        }
    }
}

