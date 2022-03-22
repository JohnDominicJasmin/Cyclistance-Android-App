package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.presentation.authentication_email.EmailAuthViewModel
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.AppImageIcon
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpViewModel
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_mapping.presentation.MappingViewModel
import timber.log.Timber

@Composable
fun SignUpScreen(navController: NavController?) {
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val mappingViewModel: MappingViewModel = hiltViewModel()

    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue("")) }

    val signUpState by remember { signUpViewModel.createAccountState }
    val hasAccountSignedIn by remember { signUpViewModel.hasAccountSignedIn }
    val context = LocalContext.current
    val isUserCreatedNewAccount =  email.value.text != mappingViewModel.email.value




    signUpState.authState.result?.let { signUpIsSuccessful ->
        LaunchedEffect(key1 = signUpIsSuccessful) {

            if (signUpIsSuccessful) {
                signUpViewModel.saveAccount()
                navController?.navigate(Screens.EmailAuthScreen.route) {
                    popUpTo(Screens.SignUpScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                Timber.e("Sign up not success.")
            }
        }
    }






    if(signUpState.internetExceptionMessage.isNotEmpty()){
        //Show dialog
        Toast.makeText(context,"No internet", Toast.LENGTH_SHORT).show()
    }
    if(signUpState.userCollisionExceptionMessage.isNotEmpty()){
        //Show dialog
        Toast.makeText(context, "Collision", Toast.LENGTH_SHORT).show()
    }



    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)) {

        ConstraintLayout(
            constraintSet = signUpConstraints,
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)) {

            AppImageIcon(layoutId = AuthenticationConstraintsItem.IconDisplay.layoutId)
            SignUpTextArea()

            Waves(
                topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
            )

            SignUpTextFieldsSection(
                email = email,
                emailOnValueChange = {
                    email.value = it
                    if (signUpState.emailExceptionMessage.isNotEmpty()) {
                        signUpViewModel.clearState()
                    }
                },
                password = password,
                passwordOnValueChange = {
                    password.value = it
                    if (signUpState.passwordExceptionMessage.isNotEmpty()) {
                        signUpViewModel.clearState()
                    }
                },
                confirmPassword = confirmPassword,
                confirmPasswordOnValueChange = {
                    confirmPassword.value = it
                    if (signUpState.confirmPasswordExceptionMessage.isNotEmpty()) {
                        signUpViewModel.clearState()
                    }
                },
                inputResultState = signUpState
            )


            SignUpButton(onClickButton = {
                if(hasAccountSignedIn && isUserCreatedNewAccount){
                    mappingViewModel.signOutAccount()
                }

                signUpViewModel.createUserWithEmailAndPassword(
                    authModel = AuthModel(
                        email = email.value.text,
                        password = password.value.text,
                        confirmPassword = confirmPassword.value.text
                    ))
            })

            SignUpClickableText() {
                navController?.navigate(Screens.SignInScreen.route) {
                    popUpTo(Screens.SignUpScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            }


        }

        if (signUpState.authState.isLoading ) {
            CircularProgressIndicator(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
        }
    }





}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = null)
}