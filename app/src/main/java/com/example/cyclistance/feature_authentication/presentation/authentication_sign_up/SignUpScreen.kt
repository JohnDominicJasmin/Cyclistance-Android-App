package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogState
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpButton
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpClickableText
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpTextArea
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpTextFieldsArea
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.signUpConstraints
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event.SignUpEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event.SignUpUiEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.event.SignUpVmEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.state.SignUpState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.state.SignUpUiState
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.visible
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val signUpState by signUpViewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var uiState by rememberSaveable { mutableStateOf(SignUpUiState()) }

    val signUpAccount = remember(key1 = uiState.email, key2 = signUpState.hasAccountSignedIn) {
        {
            val isUserCreatedNewAccount = uiState.email != signUpState.savedAccountEmail
            if (signUpState.hasAccountSignedIn && isUserCreatedNewAccount) {
                signUpViewModel.onEvent(SignUpVmEvent.SignOut)
            }
            signUpViewModel.onEvent(
                SignUpVmEvent.SignUp(
                    email = uiState.email,
                    password = uiState.password,
                    confirmPassword = uiState.confirmPassword))
        }

    }

    val onDismissAlertDialog = remember {
        {
            uiState = uiState.copy(
                alertDialogState = AlertDialogState()
            )
        }
    }

    val onDoneKeyboardAction = remember{
        {
            signUpAccount()
            focusManager.clearFocus()
        }
    }
    val onValueChangeEmail = remember {
        { inputEmail: String ->
            uiState = uiState.copy(
                email = inputEmail,
                emailErrorMessage = ""
            )
        }
    }
    val onValueChangePassword = remember {
        { inputPassword: String ->
            uiState = uiState.copy(
                password = inputPassword,
                passwordErrorMessage = ""
            )
        }
    }
    val onValueChangeConfirmPassword = remember {
        { inputConfirmPassword: String ->
            uiState = uiState.copy(
                confirmPassword = inputConfirmPassword,
                confirmPasswordErrorMessage = ""
            )
        }
    }
    val onClickPasswordVisibility = remember {
        {
            uiState = uiState.copy(
                passwordVisible = !uiState.passwordVisible
            )
        }
    }
    val onClickSignUpButton = remember {
        {
            signUpAccount()
        }
    }
    val onClickSignUpText = remember {
        {
            navController.navigateScreenInclusively(
                Screens.SignInScreen.route,
                Screens.SignUpScreen.route)
        }
    }
    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(
                isNoInternetVisible = false
            )
        }
    }

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        signUpViewModel.eventFlow.collectLatest { event ->

            when (event) {
                is SignUpEvent.SignUpSuccess -> {
                    navController.navigateScreenInclusively(
                        Screens.EmailAuthScreen.route,
                        Screens.SignUpScreen.route)

                }

                is SignUpEvent.CreateAccountFailed -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Failed to create account",
                            description = " Failed to create account. Check info and try again or contact support.",
                            icon = R.raw.error
                        )
                    )

                }

                is SignUpEvent.NoInternetConnection -> {
                    uiState = uiState.copy(
                        isNoInternetVisible = true
                    )
                }

                is SignUpEvent.AccountAlreadyTaken -> {
                    uiState = uiState.copy(
                        alertDialogState = AlertDialogState(
                            title = "Account already taken",
                            description = "Account already taken. Try again or contact support.",
                            icon = R.raw.error
                        )
                    )
                }

                is SignUpEvent.InvalidEmail -> {
                    uiState = uiState.copy(
                        emailErrorMessage = event.reason
                    )
                }

                is SignUpEvent.InvalidPassword -> {
                    uiState = uiState.copy(
                        passwordErrorMessage = event.reason
                    )
                }

                is SignUpEvent.InvalidConfirmPassword -> {
                    uiState = uiState.copy(
                        confirmPasswordErrorMessage = event.reason
                    )
                }
            }
        }
    }




    SignUpScreenContent(
        modifier = Modifier.padding(paddingValues),
        focusRequester = focusRequester,
        signUpState = signUpState,
        uiState = uiState,
        event = { event ->
            when (event) {
                is SignUpUiEvent.DismissAlertDialog -> onDismissAlertDialog()
                is SignUpUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is SignUpUiEvent.KeyboardActionDone -> onDoneKeyboardAction()
                is SignUpUiEvent.ChangeEmail -> onValueChangeEmail(event.email)
                is SignUpUiEvent.ChangePassword -> onValueChangePassword(event.password)
                is SignUpUiEvent.ChangeConfirmPassword -> onValueChangeConfirmPassword(event.confirmPassword)
                is SignUpUiEvent.TogglePasswordVisibility -> onClickPasswordVisibility()
                is SignUpUiEvent.SignUpWithEmailAndPassword -> onClickSignUpButton()
                is SignUpUiEvent.NavigateToSignIn -> onClickSignUpText()
            }
        }
    )
}


@Preview(device = Devices.PIXEL_4)
@Composable
fun SignUpScreenPreview() {
    CyclistanceTheme(true) {
        SignUpScreenContent(signUpState = SignUpState())
    }

}


@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    signUpState: SignUpState = SignUpState(),
    focusRequester: FocusRequester = FocusRequester(),
    uiState: SignUpUiState = SignUpUiState(),
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
                    .height(100.dp)
                    .width(90.dp)
                    .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId)
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
                    event(SignUpUiEvent.ChangeEmail(it))
                },
                onValueChangePassword = {
                    event(SignUpUiEvent.ChangePassword(it))
                },
                onValueChangeConfirmPassword = {
                    event(SignUpUiEvent.ChangeConfirmPassword(it))
                },
                onClickPasswordVisibility = {
                    event(SignUpUiEvent.TogglePasswordVisibility)
                },
                email = uiState.email,
                emailErrorMessage = uiState.emailErrorMessage,
                password = uiState.password,
                passwordErrorMessage = uiState.passwordErrorMessage,
                confirmPassword = uiState.confirmPassword,
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

