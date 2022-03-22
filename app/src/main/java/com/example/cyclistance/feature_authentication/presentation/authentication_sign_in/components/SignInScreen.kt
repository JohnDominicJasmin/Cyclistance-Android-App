package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import android.widget.Toast
import androidx.annotation.RawRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.input.TextFieldValue
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

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
    navController: NavController?) {

    val signInViewModel: SignInViewModel = hiltViewModel()
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val emailAuthViewModel: EmailAuthViewModel = hiltViewModel()

    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }

    val signInState by remember { signInViewModel.signInWithEmailAndPasswordState }
    val emailAuthState by remember { emailAuthViewModel.reloadState }
    val isEmailVerified by remember { emailAuthViewModel.isEmailVerified }


    signInState.authState.result?.let { signInIsSuccessful ->
        LaunchedEffect(key1 = signInIsSuccessful) {
            if (signInIsSuccessful) {
                signUpViewModel.saveAccount()
                emailAuthViewModel.reloadEmail()

            }

        }
    }

    emailAuthState.result?.let { reloadEmailIsSuccessful ->
        isEmailVerified?.let { emailVerificationIsSuccessful ->
            LaunchedEffect(key1 = reloadEmailIsSuccessful, key2 = emailVerificationIsSuccessful) {
                if (reloadEmailIsSuccessful && emailVerificationIsSuccessful) {
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
    }


    if (signInState.internetExceptionMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, "No internet", Toast.LENGTH_SHORT).show()
    }
    if (signInState.invalidUserExceptionMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, "Invalid User.", Toast.LENGTH_SHORT).show()
    }
    if (signInState.userCollisionExceptionMessage.isNotEmpty()) {
        Toast.makeText(LocalContext.current, "User Collide", Toast.LENGTH_SHORT).show()
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
            Waves(
                topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
            )

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

        if (signInState.authState.isLoading || emailAuthState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

