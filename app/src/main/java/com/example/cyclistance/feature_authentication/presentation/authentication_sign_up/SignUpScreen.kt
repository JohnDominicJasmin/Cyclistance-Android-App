package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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

    val signUpState by signUpViewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    var email by rememberSaveable { mutableStateOf("") }
    var emailErrorMessage by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordErrorMessage by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var confirmPasswordErrorMessage by rememberSaveable { mutableStateOf("") }

    var alertDialogState by remember { mutableStateOf(AlertDialogState()) }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    var isNoInternetDialogVisible by remember { mutableStateOf(false) }


    val signUpAccount = remember(key1 = email, key2 = signUpState.hasAccountSignedIn) {
        {
            val isUserCreatedNewAccount = email != signUpState.savedAccountEmail
            if (signUpState.hasAccountSignedIn && isUserCreatedNewAccount) {
                signUpViewModel.onEvent(SignUpEvent.SignOut)
            }
            signUpViewModel.onEvent(
                SignUpEvent.SignUp(
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword))
        }

    }

    val onDismissAlertDialog = remember {
        {
            alertDialogState = AlertDialogState()
        }
    }

    val onDoneKeyboardAction = remember<KeyboardActionScope.() -> Unit> {
        {
            signUpAccount()
            focusManager.clearFocus()
        }
    }
    val onValueChangeEmail = remember {
        { inputEmail: String ->
            email = inputEmail
            emailErrorMessage = ""
        }
    }
    val onValueChangePassword = remember {
        { inputPassword: String ->
            password = inputPassword
            passwordErrorMessage = ""
        }
    }
    val onValueChangeConfirmPassword = remember {
        { inputConfirmPassword: String ->
            confirmPassword = inputConfirmPassword
            confirmPasswordErrorMessage = ""
        }
    }
    val onClickPasswordVisibility = remember {
        {
            passwordVisibility = !passwordVisibility
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
            isNoInternetDialogVisible = false
        }
    }

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        signUpViewModel.eventFlow.collectLatest { event ->

            when (event) {
                is SignUpUiEvent.SignUpSuccess -> {
                    navController.navigateScreenInclusively(
                        Screens.EmailAuthScreen.route,
                        Screens.SignUpScreen.route)

                }

                is SignUpUiEvent.CreateAccountFailed -> {
                    alertDialogState = AlertDialogState(
                        title = "Failed to create account",
                        description = " Failed to create account. Check info and try again or contact support.",
                        icon = R.raw.error
                    )
                }

                is SignUpUiEvent.NoInternetConnection -> {
                    isNoInternetDialogVisible = true
                }

                is SignUpUiEvent.AccountAlreadyTaken -> {
                    alertDialogState = AlertDialogState(
                        title = "Account already taken",
                        description = "Account already taken. Try again or contact support.",
                        icon = R.raw.error
                    )
                }

                is SignUpUiEvent.InvalidEmail -> {
                    emailErrorMessage = event.reason
                }

                is SignUpUiEvent.InvalidPassword -> {
                    passwordErrorMessage = event.reason
                }

                is SignUpUiEvent.InvalidConfirmPassword -> {
                    confirmPasswordErrorMessage = event.reason
                }


            }
        }
    }




    SignUpScreenContent(
        modifier = Modifier.padding(paddingValues),
        focusRequester = focusRequester,
        signUpState = signUpState,
        onDismissAlertDialog = onDismissAlertDialog,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        keyboardActionOnDone = onDoneKeyboardAction,
        onValueChangeEmail = onValueChangeEmail,
        onValueChangePassword = onValueChangePassword,
        onValueChangeConfirmPassword = onValueChangeConfirmPassword,
        onClickPasswordVisibility = onClickPasswordVisibility,
        onClickSignUpButton = onClickSignUpButton,
        onClickSignUpText = onClickSignUpText,
        alertDialogState = alertDialogState,
        isInternetDialogVisible = isNoInternetDialogVisible,
        email = email,
        password = password,
        confirmPassword = confirmPassword,
        isPasswordVisible = passwordVisibility,
        emailErrorMessage = emailErrorMessage,
        passwordErrorMessage = passwordErrorMessage,
        confirmPasswordErrorMessage = confirmPasswordErrorMessage,
        )
}


@Preview(device = Devices.PIXEL_4)
@Composable
fun SignUpScreenPreview() {
    CyclistanceTheme(true) {
        SignUpScreenContent(
            signUpState = SignUpState(),
            alertDialogState = AlertDialogState(),
            email = "asddsadsadsadasw@gmail.com",
            password = "Passwordasknaisd",
            confirmPassword = "Confirm password",
            isPasswordVisible = false,
            isInternetDialogVisible = true,
            emailErrorMessage = "Email error message",
            passwordErrorMessage = "Password error message",
            confirmPasswordErrorMessage = "Confirm password error message",
            )
    }

}


@Composable
fun SignUpScreenContent(
    modifier: Modifier = Modifier,
    signUpState: SignUpState = SignUpState(),
    focusRequester: FocusRequester = FocusRequester(),
    alertDialogState: AlertDialogState = AlertDialogState(),
    isInternetDialogVisible: Boolean,
    email: String,
    emailErrorMessage: String,
    password: String,
    passwordErrorMessage: String,
    confirmPassword: String,
    confirmPasswordErrorMessage: String,
    isPasswordVisible: Boolean,
    onDismissAlertDialog: () -> Unit = {},
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit) = {},
    onValueChangeEmail: (String) -> Unit = {},
    onValueChangePassword: (String) -> Unit = { },
    onValueChangeConfirmPassword: (String) -> Unit = {},
    onClickPasswordVisibility: () -> Unit = {},
    onClickSignUpButton: () -> Unit = {},
    onClickSignUpText: () -> Unit = {},
    onDismissNoInternetDialog: () -> Unit = {}
) {



    Surface(color = MaterialTheme.colors.background, modifier = modifier.fillMaxSize()){

    ConstraintLayout(
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



        if (alertDialogState.visible()) {
            AlertDialog(
                alertDialog = alertDialogState,
                onDismissRequest = onDismissAlertDialog)
        }

        SignUpTextFieldsArea(
            focusRequester = focusRequester,
            state = signUpState,
            keyboardActionOnDone = keyboardActionOnDone,
            onValueChangeEmail = onValueChangeEmail,
            onValueChangePassword = onValueChangePassword,
            onValueChangeConfirmPassword = onValueChangeConfirmPassword,
            onClickPasswordVisibility = onClickPasswordVisibility,
            email = email,
            emailErrorMessage = emailErrorMessage,
            password = password,
            passwordErrorMessage = passwordErrorMessage,
            confirmPassword = confirmPassword,
            confirmPasswordErrorMessage = confirmPasswordErrorMessage,
            passwordVisibility = isPasswordVisible
        )


        SignUpButton(enabled = !signUpState.isLoading, onClickSignUpButton = onClickSignUpButton)
        SignUpClickableText(enabled = !signUpState.isLoading, onSignUpTextClick = onClickSignUpText)


        if (signUpState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId))
        }

        if (isInternetDialogVisible) {
            NoInternetDialog(
                onDismiss = onDismissNoInternetDialog,
                modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId),
            )
        }

    }
    }
}

