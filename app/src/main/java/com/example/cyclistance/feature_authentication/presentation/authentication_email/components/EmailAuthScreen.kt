package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_mapping.presentation.MappingViewModel
import androidx.compose.ui.layout.layoutId
import com.example.cyclistance.common.AlertDialogData
import com.example.cyclistance.common.SetupAlertDialog
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.navigation.Screens
import io.github.farhanroy.composeawesomedialog.R

@Composable
fun EmailAuthScreen(
    navController: NavController?,
    mappingViewModel: MappingViewModel = hiltViewModel(),
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel()) {


    LaunchedEffect(key1 = Unit) {

        emailAuthViewModel.sendEmailVerification()
        emailAuthViewModel.refreshEmailAsync()
    }


    val email = remember { mappingViewModel.getEmail() }

    val emailAuthState by remember{ emailAuthViewModel.state }
    val secondsRemainingText by derivedStateOf { if (emailAuthState.isTimerRunning) "Resend E-mail in ${emailAuthState.secondsLeft}" + if (emailAuthState.secondsLeft < 2) "" else "s" else "Resend E-mail" }

    var isEmailResendClicked by remember { mutableStateOf(false) }

    val emailVerifyState by remember {emailAuthViewModel.verifyEmailState}
    val emailReloadState by remember {emailAuthViewModel.reloadEmailState}
    val sendEmailVerificationState by remember {emailAuthViewModel.sendEmailVerificationState}








    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)) {


        ConstraintLayout(
            constraintSet = emailAuthConstraints,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .background(BackgroundColor)) {

        LaunchedEffect(key1 = emailVerifyState.result) {
            emailVerifyState.result?.let { isSuccessful ->
                if (isSuccessful) {
                    navController?.navigate(Screens.MappingScreen.route) {
                        popUpTo(Screens.EmailAuthScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }


            LaunchedEffect(key1 = emailReloadState.internetExceptionMessage) {
                emailReloadState.internetExceptionMessage.let { message ->
                    if (message.isNotEmpty()) {

                        navController?.navigate(Screens.NoInternetScreen.route) {
                            launchSingleTop = true
                        }
                    }
                }
            }

            LaunchedEffect(key1 = emailReloadState.result) {
                if (emailReloadState.result == true) {
                    emailAuthViewModel.verifyEmail()
                }
            }


            EmailIcon()
            EmailAuthTextStatus(email = email)

            if (sendEmailVerificationState.isLoading || emailVerifyState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(
                        AuthenticationConstraintsItem.ProgressBar.layoutId))
            }



            if (isEmailResendClicked) {

                if (sendEmailVerificationState.result == true) {
                    SetupAlertDialog(
                        alertDialog = AlertDialogData(
                            title = "New email sent.",
                            description = "New verification email has been sent to your email address.",
                            resId = R.raw.success
                        ))
                }

                if(sendEmailVerificationState.sendEmailExceptionMessage.isNotEmpty()) {
                    SetupAlertDialog(
                        alertDialog = AlertDialogData(
                            title = "Error",
                            description = "There was an error trying to send the verification email. Please try again.",
                            resId = R.raw.error
                        ))
                }

            }


            EmailAuthResendButton(
                text = secondsRemainingText,
                isEnabled = true,
                onClick = {
                    //emailAuthViewModel.startTimer()
                    emailAuthViewModel.sendEmailVerification()
                    isEmailResendClicked = true
                })


        }

    }
}


