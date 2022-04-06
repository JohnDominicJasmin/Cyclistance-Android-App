package com.example.cyclistance.feature_authentication.presentation.common

import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.cyclistance.theme.DisabledColor
import com.example.cyclistance.theme.TextFieldBackgroundColor
import com.example.cyclistance.theme.ThemeColor

@Composable
fun TextFieldColors() = TextFieldDefaults.textFieldColors(
    textColor = Color.White,
    backgroundColor = TextFieldBackgroundColor,
    focusedIndicatorColor = ThemeColor,
    unfocusedIndicatorColor = TextFieldBackgroundColor,
    disabledTextColor = DisabledColor,
    cursorColor = Color.White,


)
