package com.myapp.cyclistance.feature_authentication.presentation.common

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import com.myapp.cyclistance.theme.Black440

@Composable
fun textFieldColors() = TextFieldDefaults.textFieldColors(
    textColor = MaterialTheme.colors.onSecondary,
    backgroundColor = MaterialTheme.colors.secondary,
    focusedIndicatorColor = MaterialTheme.colors.primary,
    unfocusedIndicatorColor = MaterialTheme.colors.secondary,
    disabledTextColor = Black440,
    cursorColor = MaterialTheme.colors.onSecondary.copy(alpha = 0.5f),
    errorIndicatorColor = MaterialTheme.colors.error,
    errorCursorColor = MaterialTheme.colors.error,
    errorLabelColor = MaterialTheme.colors.error,
    errorLeadingIconColor = MaterialTheme.colors.error,
    errorTrailingIconColor = MaterialTheme.colors.error

)
