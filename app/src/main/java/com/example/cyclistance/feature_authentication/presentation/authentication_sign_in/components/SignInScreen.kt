package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInViewModel
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.theme.*

@Composable
fun SignInScreen(
    navController: NavController?) {

    val signInViewModel: SignInViewModel = hiltViewModel()
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val signInState by remember { signInViewModel.signInWithEmailAndPasswordState }

    ConstraintLayout(
        constraintSet = signInConstraints, modifier = Modifier
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
            emailExceptionMessage = signInState.emailExceptionMessage,
            password = password,
            passwordExceptionMessage = signInState.passwordExceptionMessage,

            passwordOnValueChange = {
                password.value = it
                if(signInState.passwordExceptionMessage.isNotEmpty()){
                    signInViewModel.textFieldOnValueChanged()
                }
            },
            emailOnValueChange = {
                email.value = it
                if(signInState.emailExceptionMessage.isNotEmpty()){
                    signInViewModel.textFieldOnValueChanged()
                }
            }
        )

        SignInGoogleAndFacebookSection(
            facebookButtonOnClick = {

            },
            googleSignInButtonOnClick = {

            }
        )

        SignInButton(onClickButton = {
            signInViewModel.signInWithEmailAndPassword(authModel = AuthModel(
                email = email.value.text,
                password = password.value.text))
        })


        SignInClickableText(onClick = {
            navController?.navigate(Screens.SignUpScreen.route)
        })

    }


}


