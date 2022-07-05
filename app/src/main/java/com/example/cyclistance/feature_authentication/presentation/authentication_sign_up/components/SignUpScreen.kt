package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cyclistance.R
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialogModel
import com.example.cyclistance.feature_alert_dialog.presentation.SetupAlertDialog
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpUiEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpViewModel
import com.example.cyclistance.feature_authentication.presentation.common.Waves
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    mappingViewModel: MappingViewModel = hiltViewModel(),
    navigateTo : (destination: String, popUpToDestination: String?) -> Unit) {

    val signUpStateValue = signUpViewModel.state
    val focusManager = LocalFocusManager.current
    with(signUpStateValue) {

        val hasAccountSignedIn = remember { signUpViewModel.hasAccountSignedIn() }
        val isUserCreatedNewAccount = remember { email.text != mappingViewModel.getEmail() }
        var alertDialogState by remember { mutableStateOf(AlertDialogModel()) }
        val context = LocalContext.current

        val signUpAccount = {
            if (hasAccountSignedIn && isUserCreatedNewAccount) {
                signUpViewModel.onEvent(SignUpEvent.SignOut)
            }
            signUpViewModel.onEvent(SignUpEvent.SignUp)
        }


        LaunchedEffect(key1 = true){
            signUpStateValue.focusRequester.requestFocus()
            signUpViewModel.eventFlow.collectLatest{ event ->

                when(event){
                    is SignUpUiEvent.ShowNoInternetScreen -> {
                        navigateTo(Screens.NoInternetScreen.route, null)
                    }
                    is SignUpUiEvent.ShowAlertDialog -> {
                        alertDialogState = AlertDialogModel(
                            title = event.title,
                            description = event.description,
                            resId = event.imageResId)
                    }
                    is SignUpUiEvent.ShowEmailAuthScreen -> {
                        navigateTo(Screens.EmailAuthScreen.route, Screens.SignUpScreen.route)
                    }
                    is SignUpUiEvent.ShowToastMessage -> {
                        Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }




        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {

            ConstraintLayout(
                constraintSet = signUpConstraints,
                modifier = Modifier
                    .fillMaxSize()
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

                Waves(
                    topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
                    bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
                )

                if (alertDialogState.run { title.isNotEmpty() || description.isNotEmpty() }) {
                    SetupAlertDialog(
                        alertDialog = alertDialogState,
                        onDismissRequest = {
                            alertDialogState = AlertDialogModel()
                        })
                }

                SignUpTextFieldsArea(
                    focusRequester = signUpStateValue.focusRequester,
                    state = this@with,
                    signUpViewModel = signUpViewModel,
                    keyboardActionOnDone = {
                        signUpAccount()
                        focusManager.clearFocus()
                    }
                )


                SignUpButton(onClickButton = {
                    signUpAccount()
                })

                SignUpClickableText() {
                    navigateTo(Screens.SignInScreen.route, Screens.SignUpScreen.route)
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
                    )
                }
            }
        }
    }

}

@Preview(device = Devices.NEXUS_5)
@Composable
fun SignUpScreenPreview() {

    CyclistanceTheme(false) {

        Column(

            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .background(androidx.compose.material.MaterialTheme.colors.background)) {

            ConstraintLayout(
                constraintSet = com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.signUpConstraints,
                modifier = androidx.compose.ui.Modifier
                    .fillMaxSize()
                    .background(androidx.compose.material.MaterialTheme.colors.background)) {

                Image(
                    contentDescription = "App Icon",
                    painter = painterResource(com.example.cyclistance.R.drawable.ic_app_icon_cyclistance),
                    modifier = androidx.compose.ui.Modifier
                        .height(100.dp)
                        .width(90.dp)
                        .layoutId(com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem.IconDisplay.layoutId)
                )

                SignUpTextArea()

                Waves(
                    topWaveLayoutId = com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem.TopWave.layoutId,
                    bottomWaveLayoutId = com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem.BottomWave.layoutId
                )



                SignUpTextFieldsArea(focusRequester = FocusRequester(),
                    state = SignUpState(email = TextFieldValue("Testing@gmail,com")),
                    signUpViewModel = null,
                    keyboardActionOnDone = { }
                )


                SignUpButton(onClickButton = {
                })

                SignUpClickableText() {
                }

                if (true) {
                    CircularProgressIndicator(
                        modifier = androidx.compose.ui.Modifier.layoutId(com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem.ProgressBar.layoutId)
                    )
                }
            }
        }
    }
}

