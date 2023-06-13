package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.authentication_email.state.EmailAuthState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.event.SignUiEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.state.SignInState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.state.SignInUiState
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.feature_dialogs.presentation.alert_dialog.AlertDialog
import com.example.cyclistance.feature_dialogs.presentation.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.theme.CyclistanceTheme


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewSignInScreenDark() {

    var uiState by rememberSaveable {
        mutableStateOf(SignInUiState())
    }


    val onValueChangeEmail = remember<(String) -> Unit> {
        {
            uiState = uiState.copy(
                email = it,
                emailErrorMessage = ""
            )
        }
    }

    val onValueChangePassword = remember<(String) -> Unit> {
        {
            uiState = uiState.copy(
                password = it,
                passwordErrorMessage = ""
            )
        }
    }

    CyclistanceTheme(true) {
        SignInScreenContent(uiState = uiState, event = {
            when (it) {
                is SignUiEvent.ChangeEmail -> onValueChangeEmail(it.email)
                is SignUiEvent.ChangePassword -> onValueChangePassword(it.password)
                else -> {}
            }
        })
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewSignInScreenLight() {

    var uiState by rememberSaveable {
        mutableStateOf(SignInUiState())
    }


    val onValueChangeEmail = remember<(String) -> Unit> {
        {
            uiState = uiState.copy(
                email = it,
                emailErrorMessage = ""
            )
        }
    }

    val onValueChangePassword = remember<(String) -> Unit> {
        {
            uiState = uiState.copy(
                password = it,
                passwordErrorMessage = ""
            )
        }
    }


    CyclistanceTheme(false) {
        SignInScreenContent(uiState = uiState, event = {
            when (it) {
                is SignUiEvent.ChangeEmail -> onValueChangeEmail(it.email)
                is SignUiEvent.ChangePassword -> onValueChangePassword(it.password)
                else -> {}
            }
        })
    }
}


@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester(),
    signInState: SignInState = SignInState(),
    emailAuthState: EmailAuthState = EmailAuthState(),
    uiState: SignInUiState = SignInUiState(),
    event: (SignUiEvent) -> Unit = {}) {


    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        ConstraintLayout(
            constraintSet = signInConstraints,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {

            Spacer(modifier = Modifier.layoutId(AuthenticationConstraintsItem.TopSpacer.layoutId))

            Image(
                contentDescription = "App Icon",
                painter = painterResource(R.drawable.ic_app_icon_cyclistance),
                modifier = Modifier
                    .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId)
            )


            SignUpTextArea()

            if (uiState.alertDialogState.visible()) {
                AlertDialog(
                    alertDialog = uiState.alertDialogState,
                    onDismissRequest = {
                        event(SignUiEvent.DismissAlertDialog)
                    })
            }

            Waves(
                topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId,
            )

            SignInTextFieldsArea(
                focusRequester = focusRequester,
                state = signInState,
                keyboardActionOnDone = {
                    event(SignUiEvent.KeyboardActionDone)
                },
                onValueChangeEmail = { event(SignUiEvent.ChangeEmail(it)) },
                onValueChangePassword = { event(SignUiEvent.ChangePassword(it)) },
                onClickPasswordVisibility = { event(SignUiEvent.TogglePasswordVisibility) },
                email = uiState.email,
                emailErrorMessage = uiState.emailErrorMessage,
                password = uiState.password,
                passwordErrorMessage = uiState.passwordErrorMessage,
                passwordVisible = uiState.isPasswordVisible
            )

            val isLoading = remember(signInState.isLoading, emailAuthState.isLoading) {
                (signInState.isLoading || emailAuthState.isLoading)
            }

            SignInCredentialsSection(
                onClickFacebookButton = { event(SignUiEvent.SignInWithFacebook) },
                onClickGoogleButton = { event(SignUiEvent.SignInWithGoogle) },
                enabled = !isLoading
            )

            SignInButton(
                onClickSignInButton = { event(SignUiEvent.SignInWithEmailAndPassword) },
                enabled = !isLoading)

            SignInClickableText(
                onClickSignInText = { event(SignUiEvent.NavigateToSignUp) },
                enabled = !isLoading)

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
                )
            }

            if (uiState.isNoInternetVisible) {
                NoInternetDialog(
                    onDismiss = {
                        event(SignUiEvent.DismissNoInternetDialog)
                    },
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId))

            }

        }
    }
}





