package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId

import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.common.AlertDialogData
import com.example.cyclistance.common.SetupAlertDialog

import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInEventResult
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInViewModel
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.theme.*
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SignInScreen(
    navController: NavController?,
    signInViewModel: SignInViewModel = hiltViewModel(),
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel()) {




    val email = signInViewModel.email
    val password = signInViewModel.password
    var isLoading by remember { mutableStateOf(false) }
    var alertDialogState by remember { mutableStateOf(AlertDialogData()) }
    var emailExceptionMessage by remember { mutableStateOf("") }
    var passwordExceptionMessage by remember { mutableStateOf("") }

    val emailReloadState by remember { emailAuthViewModel.reloadEmailState }
    val emailVerifyState by remember { emailAuthViewModel.verifyEmailState }




    LaunchedEffect(key1 = true) {
        signInViewModel.eventFlow.collectLatest { event ->


            when (event){
                is SignInEventResult.RefreshEmail -> {
                    emailAuthViewModel.refreshEmail()
                }
                is SignInEventResult.ShowInternetScreen -> {
                    navController?.navigate(Screens.NoInternetScreen.route) {
                        launchSingleTop = true
                    }
                }
                is SignInEventResult.ShowProgressBar -> {
                    isLoading = true
                }
                is SignInEventResult.HideProgressBar -> {
                    isLoading = false
                }
                is SignInEventResult.ShowAlertDialog -> {
                    alertDialogState = AlertDialogData(
                        title = event.title,
                        description = event.description,
                        resId = event.imageResId)
                }
                is SignInEventResult.ShowEmailTextFieldError -> {
                    emailExceptionMessage = event.errorMessage
                }
                is SignInEventResult.ShowPasswordTextFieldError -> {
                    passwordExceptionMessage = event.errorMessage
                }





            }
        }
    }


    LaunchedEffect(key1 = emailReloadState.result) {
        emailReloadState.result?.let { reloadEmailIsSuccessful ->
            if (reloadEmailIsSuccessful) {
                emailAuthViewModel.verifyEmail()
            }
        }
    }


    LaunchedEffect(key1 = emailVerifyState.result) {
        emailVerifyState.result?.let { isSuccessful ->
            if (isSuccessful) {
                navController?.navigate(Screens.MappingScreen.route) {
                    popUpTo(Screens.SignInScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                navController?.navigate(Screens.EmailAuthScreen.route) {
                    popUpTo(Screens.SignInScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }










    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)) {

        ConstraintLayout(
            constraintSet = signInConstraints,
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)) {


            AppImageIcon(layoutId = AuthenticationConstraintsItem.IconDisplay.layoutId)
            SignUpTextArea()

            if(alertDialogState.title.isNotEmpty()) {
                SetupAlertDialog(alertDialog = alertDialogState)
            }

            Waves(
                topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
            )





            SignInTextFieldsArea(
                email = email.value,
                emailOnValueChange = { signInViewModel.onEvent(SignInEvent.EnteredEmail(email = it)) },
                emailExceptionMessage = emailExceptionMessage,
                emailClearIconOnClick = { signInViewModel.onEvent(SignInEvent.ClearEmailErrorMessage) },
                password = password.value,
                passwordOnValueChange = { signInViewModel.onEvent(SignInEvent.EnteredPassword(password = it)) },
                passwordExceptionMessage = passwordExceptionMessage,
                keyboardActionOnDone = { signInViewModel.onEvent(SignInEvent.SignInDefault) })

            SignInGoogleAndFacebookSection(
                facebookButtonOnClick = {

                },
                googleSignInButtonOnClick = {

                }
            )

            SignInButton(onClickButton = {
                signInViewModel.onEvent(SignInEvent.SignInDefault)
            })


            SignInClickableText(onClick = {
                navController?.navigate(Screens.SignUpScreen.route)
            })

            if (isLoading || emailReloadState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
                )
            }

        }


    }
}



