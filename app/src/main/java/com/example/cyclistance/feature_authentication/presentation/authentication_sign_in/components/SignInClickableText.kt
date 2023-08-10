package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.DONT_HAVE_ACCOUNT_ID
import com.example.cyclistance.theme.Blue600

@Composable
fun SignInClickableText(enabled: Boolean, onClickSignInText: () -> Unit) {

    Box(
        modifier = Modifier
            .wrapContentSize()
            .layoutId(DONT_HAVE_ACCOUNT_ID)) {
        ClickableText(
            style = MaterialTheme.typography.subtitle2,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                    append(text = "Don't have an account?")
                }
                withStyle(style = SpanStyle(color = Blue600)) {
                    append(" ")
                    append(text = "Sign up")
                }
            }, onClick = {
                if (enabled) {
                    onClickSignInText()
                }
            })
    }

}

