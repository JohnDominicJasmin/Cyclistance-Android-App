package com.example.cyclistance.feature_authentication.presentation.common

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.cyclistance.theme.*

@Composable
fun textFieldColors() = TextFieldDefaults.textFieldColors(
    textColor = MaterialTheme.colors.onSecondary,
    backgroundColor = MaterialTheme.colors.secondary,
    focusedIndicatorColor = MaterialTheme.colors.primary,
    unfocusedIndicatorColor = MaterialTheme.colors.secondary,
    disabledTextColor = Black440,
    cursorColor = MaterialTheme.colors.onSecondary,
    errorIndicatorColor = MaterialTheme.colors.error,
    errorCursorColor = MaterialTheme.colors.error,
    errorLabelColor = MaterialTheme.colors.error,
    errorLeadingIconColor = MaterialTheme.colors.error,
    errorTrailingIconColor = MaterialTheme.colors.error

)
