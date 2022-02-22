package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.cyclistance.feature_authentication.presentation.theme.TextFieldBackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor

@Composable
fun TextFieldColors() = TextFieldDefaults.textFieldColors(
    textColor = Color.White,
    backgroundColor = TextFieldBackgroundColor,
    focusedIndicatorColor = TextFieldBackgroundColor,
    unfocusedIndicatorColor = TextFieldBackgroundColor,
    cursorColor = Color.White

)
