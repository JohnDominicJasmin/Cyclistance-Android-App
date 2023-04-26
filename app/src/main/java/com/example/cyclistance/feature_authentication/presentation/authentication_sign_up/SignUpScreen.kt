package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.*
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

    val context = LocalContext.current

    val signUpAccount = remember(key1 = signUpState.email, key2 = signUpState.email, key3 = signUpState.hasAccountSignedIn) {
        {
            val isUserCreatedNewAccount = signUpState.email != signUpState.savedAccountEmail
            if (signUpState.hasAccountSignedIn && isUserCreatedNewAccount) {
                signUpViewModel.onEvent(SignUpEvent.SignOut)
            }
            signUpViewModel.onEvent(SignUpEvent.SignUp)
        }
    }

    val onDismissAlertDialog = remember{{
        signUpViewModel.onEvent(SignUpEvent.DismissAlertDialog)
    }}

    val onDoneKeyboardAction = remember<KeyboardActionScope.() -> Unit> {{
        signUpAccount()
        focusManager.clearFocus()
    }}
    val onValueChangeEmail = remember{{ email:String ->
        signUpViewModel.onEvent(SignUpEvent.EnterEmail(email))
    }}
    val onValueChangePassword = remember{{password: String ->
        signUpViewModel.onEvent(SignUpEvent.EnterPassword(password))
    }}
    val onValueChangeConfirmPassword = remember { { confirmPassword: String ->
        signUpViewModel.onEvent(SignUpEvent.EnterConfirmPassword(confirmPassword))
    }}
    val onClickPasswordVisibility = remember{{
        signUpViewModel.onEvent(SignUpEvent.TogglePasswordVisibility)
    }}
    val onClickSignUpButton = remember{{
        signUpAccount()
    }}
    val onClickSignUpText = remember{{
        navController.navigateScreenInclusively(
            Screens.SignInScreen.route,
            Screens.SignUpScreen.route)
    }}
    val onDismissNoInternetDialog = remember{{
        signUpViewModel.onEvent(SignUpEvent.DismissNoInternetDialog)
    }}

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
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
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


    ConstraintLayout(
        constraintSet = signUpConstraints,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {

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



        if (signUpState.alertDialogModel.visible()) {
            AlertDialog(
                alertDialog = signUpState.alertDialogModel,
                onDismissRequest = onDismissAlertDialog)
        }

        SignUpTextFieldsArea(
            focusRequester = focusRequester,
            state = signUpState,
            keyboardActionOnDone = keyboardActionOnDone,
            onValueChangeEmail = onValueChangeEmail,
            onValueChangePassword = onValueChangePassword,
            onValueChangeConfirmPassword = onValueChangeConfirmPassword,
            onClickPasswordVisibility = onClickPasswordVisibility
        )


        SignUpButton(enabled = !signUpState.isLoading, onClickSignUpButton = onClickSignUpButton)
        SignUpClickableText(enabled = !signUpState.isLoading, onSignUpTextClick = onClickSignUpText)


        if (signUpState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId))
        }

        if (!signUpState.hasInternet) {
            NoInternetDialog(onDismiss = onDismissNoInternetDialog,  modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId),)
        }


    }
}

