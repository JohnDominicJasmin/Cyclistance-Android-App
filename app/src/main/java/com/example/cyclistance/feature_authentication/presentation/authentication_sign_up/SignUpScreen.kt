package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.cyclistance.core.utils.ConnectionStatus
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.*
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_no_internet.presentation.NoInternetScreen
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

    val signUpAccount = {
        val isUserCreatedNewAccount = signUpState.email != signUpState.savedAccountEmail
        if (signUpState.hasAccountSignedIn && isUserCreatedNewAccount) {
            signUpViewModel.onEvent(SignUpEvent.SignOut)
        }
        signUpViewModel.onEvent(SignUpEvent.SignUp)
    }


    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        signUpViewModel.eventFlow.collectLatest { event ->

            when (event) {
                is SignUpUiEvent.ShowEmailAuthScreen -> {
                    navController.navigateScreenInclusively(
                        Screens.EmailAuthScreen.route,
                        Screens.SignUpScreen.route)

                }
                is SignUpUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    SignUpScreen(
        modifier = Modifier.padding(paddingValues),
        focusRequester = focusRequester,
        signUpState = signUpState,
        onDismissAlertDialog = {
            signUpViewModel.onEvent(SignUpEvent.DismissAlertDialog)
        },
        keyboardActionOnDone = {
            signUpAccount()
            focusManager.clearFocus()
        },
        onValueChangeEmail = { email ->
            signUpViewModel.onEvent(SignUpEvent.EnterEmail(email))
        },
        onValueChangePassword = { password ->
            signUpViewModel.onEvent(SignUpEvent.EnterPassword(password))
        },
        onValueChangeConfirmPassword = { confirmPassword ->
            signUpViewModel.onEvent(SignUpEvent.EnterConfirmPassword(confirmPassword))
        },
        onClickPasswordVisibility = {
            signUpViewModel.onEvent(SignUpEvent.TogglePasswordVisibility)
        },
        onClickSignUpButton = {
            signUpAccount()
        },
        onClickSignUpText = {
            navController.navigateScreenInclusively(
                Screens.SignInScreen.route,
                Screens.SignUpScreen.route)
        },
        onClickRetryButton = {
            if (ConnectionStatus.hasInternetConnection(context)) {
                signUpViewModel.onEvent(event = SignUpEvent.DismissNoInternetScreen)
            }
        }


    )
}


@Preview(device = Devices.PIXEL_4)
@Composable
fun SignUpScreenPreview() {
    CyclistanceTheme(true) {
        SignUpScreen()
    }

}


@Composable
fun SignUpScreen(
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
    onClickRetryButton: () -> Unit = {}
) {

    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colors.background)) {

        Spacer(modifier = Modifier.weight(0.04f, fill = true))

        ConstraintLayout(
            constraintSet = signUpConstraints,
            modifier = Modifier
                .fillMaxSize()
                .weight(0.95f)
                .align(Alignment.CenterHorizontally)
                .background(MaterialTheme.colors.background)) {

            Image(
                contentDescription = "App Icon",
                painter = painterResource(R.drawable.ic_app_icon_cyclistance),
                modifier = Modifier
                    .height(100.dp)
                    .width(90.dp)
                    .layoutId(AuthenticationConstraintsItem.IconDisplay.layoutId)
            )

            SignUpTextArea()



            if (signUpState.alertDialogModel.run { title.isNotEmpty() || description.isNotEmpty() }) {
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


            SignUpButton(onClickSignUpButton = onClickSignUpButton)
            SignUpClickableText(onSignUpTextClick = onClickSignUpText)


            if (signUpState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId))
            }

            if (!signUpState.hasInternet) {
                NoInternetScreen(
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.NoInternetScreen.layoutId),
                    onClickRetryButton = onClickRetryButton)
            }
        }


    }
}

