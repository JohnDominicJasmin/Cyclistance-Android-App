package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.TextFieldValue
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.common.AlertDialogData
import com.example.cyclistance.common.SetupAlertDialog

import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpViewModel
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.theme.*


@Composable
fun SignInScreen(
    navController: NavController?,
    signInViewModel: SignInViewModel = hiltViewModel(),
    emailAuthViewModel: EmailAuthViewModel = hiltViewModel()
) {



    val email = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val password = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val signInState by remember { signInViewModel.signInWithEmailAndPasswordState }
    val emailReloadState by remember { emailAuthViewModel.reloadEmailState }
    val emailVerifyState by remember { emailAuthViewModel.verifyEmailState }






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
            Waves(
                topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
            )




            emailReloadState.result?.let { reloadEmailIsSuccessful ->
                if (reloadEmailIsSuccessful) {
                    emailAuthViewModel.verifyEmail()
                }
            }


            signInState.internetExceptionMessage.let { message ->
                if (message.isNotEmpty()) {
                    navController?.navigate(Screens.NoInternetScreen.route) {
                        launchSingleTop = true
                    }
                }
            }



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

            signInState.result?.let { signInIsSuccessful ->
                if (signInIsSuccessful) {
                    emailAuthViewModel.reloadEmail()
                }
            }


            signInState.invalidUserExceptionMessage.let{ message ->
                if(message.isNotEmpty()){
                    SetupAlertDialog(
                        alertDialog = AlertDialogData(
                            title = "Error",
                            description = message,
                            resId = io.github.farhanroy.composeawesomedialog.R.raw.error))
                }
            }

            signInState.userCollisionExceptionMessage.let { message ->
                if(message.isNotEmpty()){
                    SetupAlertDialog(
                        alertDialog = AlertDialogData(
                            title = "Error",
                            description = message,
                            resId = io.github.farhanroy.composeawesomedialog.R.raw.error))
                }
            }





            SignInTextFieldsSection(
                email = email,
                password = password,
                passwordOnValueChange = {
                    password.value = it
                    if (signInState.passwordExceptionMessage.isNotEmpty()) {
                        signInViewModel.clearState()
                    }
                },
                emailOnValueChange = {
                    email.value = it
                    if (signInState.emailExceptionMessage.isNotEmpty()) {
                        signInViewModel.clearState()
                    }
                },
                inputResultState = signInState
            )

            SignInGoogleAndFacebookSection(
                facebookButtonOnClick = {

                },
                googleSignInButtonOnClick = {

                }
            )

            SignInButton(onClickButton = {
                signInViewModel.signInWithEmailAndPassword(
                    authModel = AuthModel(
                        email = email.value.text,
                        password = password.value.text))
            })


            SignInClickableText(onClick = {
                navController?.navigate(Screens.SignUpScreen.route)
            })

        }

        if (signInState.isLoading || emailReloadState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

