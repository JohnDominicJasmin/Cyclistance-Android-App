package com.example.cyclistance.feature_authentication.presentation.auth_sign_in.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.core.presentation.dialogs.alert_dialog.AlertDialog
import com.example.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.core.presentation.dialogs.privacy_policy_dialog.PrivacyPolicyDialog
import com.example.cyclistance.core.presentation.dialogs.webview_dialog.DialogWebView
import com.example.cyclistance.feature_authentication.presentation.auth_email.state.EmailAuthState
import com.example.cyclistance.feature_authentication.presentation.auth_sign_in.event.SignInUiEvent
import com.example.cyclistance.feature_authentication.presentation.auth_sign_in.state.SignInState
import com.example.cyclistance.feature_authentication.presentation.auth_sign_in.state.SignInUiState
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.BOTTOM_WAVE_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.DIALOG_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.ICON_DISPLAY_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.PROGRESS_BAR_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TEXT_FIELDS_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TOP_SPACER_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TOP_WAVE_ID
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun SignInScreenContent(
    modifier: Modifier = Modifier,
    signInState: SignInState = SignInState(),
    emailAuthState: EmailAuthState = EmailAuthState(),
    uiState: SignInUiState = SignInUiState(),
    email: TextFieldValue,
    password: TextFieldValue,
    event: (SignInUiEvent) -> Unit = {}) {

    var lastClicked by remember {
        mutableStateOf<SignInUiEvent?>(null)
    }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        ConstraintLayout(
            constraintSet = signInConstraints,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {

            Spacer(modifier = Modifier.layoutId(TOP_SPACER_ID))

            Image(
                contentDescription = "App Icon",
                painter = painterResource(R.drawable.ic_app_icon_cyclistance),
                modifier = Modifier
                    .layoutId(ICON_DISPLAY_ID)
            )


            SignUpTextArea()

            if (uiState.alertDialogState.visible()) {
                AlertDialog(
                    alertDialog = uiState.alertDialogState,
                    onDismissRequest = {
                        event(SignInUiEvent.DismissAlertDialog)
                    })
            }

            Waves(
                topWaveLayoutId = TOP_WAVE_ID,
                bottomWaveLayoutId = BOTTOM_WAVE_ID,
            )

            Column(
                modifier = Modifier.layoutId(TEXT_FIELDS_ID)
            ) {
                SignInTextFieldsArea(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.9f),
                    state = signInState,
                    keyboardActionOnDone = {
                        event(SignInUiEvent.KeyboardActionDone)
                    },
                    onValueChangeEmail = { event(SignInUiEvent.OnChangeEmail(it)) },
                    onValueChangePassword = { event(SignInUiEvent.OnChangePassword(it)) },
                    onClickPasswordVisibility = { event(SignInUiEvent.TogglePasswordVisibility) },
                    email = email,
                    emailErrorMessage = uiState.emailErrorMessage,
                    password = password,
                    passwordErrorMessage = uiState.passwordErrorMessage,
                    passwordVisible = uiState.isPasswordVisible
                )
                ClickableText(modifier = Modifier.padding(top = 8.dp), text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                        append("Forgot your password?")
                    }
                }, onClick = {
                    event(SignInUiEvent.NavigateToForgotPassword)
                })
            }

            val isLoading = remember(signInState.isLoading, emailAuthState.isLoading) {
                (signInState.isLoading || emailAuthState.isLoading)
            }

            val shouldShowPrivacyPolicyDialog = remember(
                key1 = signInState.userAgreedToPrivacyPolicy,
                key2 = uiState.isPrivacyPolicyDialogVisible) {
                (!signInState.userAgreedToPrivacyPolicy && uiState.isPrivacyPolicyDialogVisible)
            }

            SignInCredentialsSection(
                onClickFacebookButton = {

                    if (signInState.userAgreedToPrivacyPolicy) {
                        event(SignInUiEvent.SignInWithFacebook)
                    }
                    else{
                        event(SignInUiEvent.SetPrivacyPolicyVisibility(true))
                        lastClicked = SignInUiEvent.SignInWithFacebook
                    }

                },
                onClickGoogleButton = {
                    if (signInState.userAgreedToPrivacyPolicy){
                        event(SignInUiEvent.SignInWithGoogle)
                    }
                    else {
                        event(SignInUiEvent.SetPrivacyPolicyVisibility(true))
                        lastClicked = SignInUiEvent.SignInWithGoogle
                    }
                },
                enabled = !isLoading
            )

            SignInButton(
                onClickSignInButton = { event(SignInUiEvent.SignInWithEmailAndPassword) },
                enabled = !isLoading)

            SignInClickableText(
                onClickSignInText = { event(SignInUiEvent.NavigateToSignUp) },
                enabled = !isLoading)

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(PROGRESS_BAR_ID)
                )
            }

            if (uiState.isNoInternetVisible) {
                NoInternetDialog(
                    onDismiss = {
                        event(SignInUiEvent.DismissNoInternetDialog)
                    },
                    modifier = Modifier.layoutId(DIALOG_ID))

            }

            if (shouldShowPrivacyPolicyDialog) {
                PrivacyPolicyDialog(
                    modifier = Modifier.layoutId(DIALOG_ID),
                    onDismiss = { event(SignInUiEvent.SetPrivacyPolicyVisibility(false)) },
                    onClickAgree = {
                        event(SignInUiEvent.AgreedToPrivacyPolicy)
                        lastClicked?.let {
                            event(it)
                        }
                    },
                    onClickLink = {
                        event(SignInUiEvent.OpenWebView(it))
                    })
            }

            if (uiState.urlToOpen != null) {
                DialogWebView(
                    modifier = Modifier
                        .fillMaxSize()
                        .layoutId(DIALOG_ID),
                    mUrl = uiState.urlToOpen,
                    onDismiss = { event(SignInUiEvent.DismissWebView) })
            }


        }
    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewSignInScreenDark() {

    var uiState by rememberSaveable {
        mutableStateOf(SignInUiState())
    }
    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val onValueChangeEmail = remember<(TextFieldValue) -> Unit> {
        {
            uiState = uiState.copy(
                emailErrorMessage = ""
            )
            email = it
        }
    }

    val onValueChangePassword = remember<(TextFieldValue) -> Unit> {
        {
            uiState = uiState.copy(
                passwordErrorMessage = ""
            )
            password = it
        }
    }

    CyclistanceTheme(true) {
        SignInScreenContent(uiState = uiState, email = email, password = password, event = {
            when (it) {
                is SignInUiEvent.OnChangeEmail -> onValueChangeEmail(it.email)
                is SignInUiEvent.OnChangePassword -> onValueChangePassword(it.password)
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
    var email by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var password by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    val onValueChangeEmail = remember<(TextFieldValue) -> Unit> {
        {
            uiState = uiState.copy(
                emailErrorMessage = ""
            )
            email = it
        }
    }

    val onValueChangePassword = remember<(TextFieldValue) -> Unit> {
        {
            uiState = uiState.copy(
                passwordErrorMessage = ""
            )
            password = it
        }
    }


    CyclistanceTheme(false) {
        SignInScreenContent(uiState = uiState, email = email, password = password, event = {
            when (it) {
                is SignInUiEvent.OnChangeEmail -> onValueChangeEmail(it.email)
                is SignInUiEvent.OnChangePassword -> onValueChangePassword(it.password)
                else -> {}
            }
        })
    }
}







