package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.R
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_alert_dialog.presentation.AlertDialog
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.*
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.MappingViewModel
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    mappingViewModel: MappingViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val signUpStateValue by signUpViewModel.state.collectAsState()
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


        LaunchedEffect(key1 = true) {
            signUpStateValue.focusRequester.requestFocus()
            signUpViewModel.eventFlow.collectLatest { event ->

                when (event) {
                    is SignUpUiEvent.ShowNoInternetScreen -> {
                        navController.navigateScreen(
                            Screens.NoInternetScreen.route,
                            Screens.SignUpScreen.route)
                    }
                    is SignUpUiEvent.ShowAlertDialog -> {
                        alertDialogState = AlertDialogModel(
                            title = event.title,
                            description = event.description,
                            icon = event.imageResId)
                    }
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




        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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



                if (alertDialogState.run { title.isNotEmpty() || description.isNotEmpty() }) {
                    AlertDialog(
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
                    navController.navigateScreenInclusively(
                        Screens.SignInScreen.route,
                        Screens.SignUpScreen.route)
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

@Preview(device = Devices.PIXEL_4)
@Composable
fun SignUpScreenPreview() {

    CyclistanceTheme(false) {

        Column(

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
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
                        modifier = Modifier.layoutId(AuthenticationConstraintsItem.ProgressBar.layoutId)
                    )
                }
            }
        }
    }
}

