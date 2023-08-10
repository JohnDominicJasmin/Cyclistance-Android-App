package com.example.cyclistance.feature_authentication.presentation.auth_forgot_password

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.components.ForgotPasswordContent

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = viewModel(),
    navController: NavController,
    paddingValues: PaddingValues
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ForgotPasswordContent(state = state, modifier = Modifier.padding(paddingValues))

}