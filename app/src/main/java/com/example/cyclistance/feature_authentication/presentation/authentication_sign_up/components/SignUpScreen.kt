package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.common.AlertDialogData
import com.example.cyclistance.common.SetupAlertDialog
import io.github.farhanroy.composeawesomedialog.R.raw.error
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.AppImageIcon
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpViewModel
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.SignUpTextFieldsSection
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_mapping.presentation.MappingViewModel
import timber.log.Timber

@Composable
fun SignUpScreen(
    navController: NavController?,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    mappingViewModel: MappingViewModel = hiltViewModel()) {


    val email = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val name = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val password = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    val confirmPassword = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }

    val signUpState by remember { signUpViewModel.createAccountState }
    val hasAccountSignedIn = remember { signUpViewModel.hasAccountSignedIn() }
    val isUserCreatedNewAccount =  remember { email.value.text != mappingViewModel.getEmail() }



    LaunchedEffect(key1 = signUpState.result) {
        signUpState.result?.let { signUpIsSuccessful ->

            if (signUpIsSuccessful) {
                navController?.popBackStack()
                navController?.navigate(Screens.EmailAuthScreen.route) {
                    popUpTo(Screens.SignUpScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            } else {
                Timber.e("Sign up not success.")
            }
        }
    }

    LaunchedEffect(key1 = signUpState.internetExceptionMessage) {
        signUpState.internetExceptionMessage.let { message ->
            if (message.isNotEmpty()) {
                navController?.navigate(Screens.NoInternetScreen.route) {
                    launchSingleTop = true
                }
            }
        }
    }









    signUpState.userCollisionExceptionMessage.let{ message->
        if(message.isNotEmpty()){

            SetupAlertDialog(
                alertDialog = AlertDialogData(
                title = "Error",
                description = message,
                resId = error
            ))

        }
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
                name = name,
                nameOnValueChange = {
                    name.value = it

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
            if (signUpState.isLoading ) {
                CircularProgressIndicator(
                    modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
                )
            }

        }


    }





}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = null)
}