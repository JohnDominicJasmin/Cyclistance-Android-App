package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.AppImageIcon
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpViewModel
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor

@Composable
fun SignUpScreen(navController: NavController?) {
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue("")) }
    val signUpState = signUpViewModel.createAccountState.value

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
            signUpViewModel.createUserWithEmailAndPassword(authModel = AuthModel(
                email = email.value.text,
                password = password.value.text,
                confirmPassword = confirmPassword.value.text
            ))
        })

        SignUpClickableText() {
            navController?.navigate(Screens.SignInScreen.route)
        }


    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = null)
}