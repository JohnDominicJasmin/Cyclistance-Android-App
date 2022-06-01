package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.theme.Blue500
import com.example.cyclistance.theme.Blue600

@Composable
fun SignInClickableText(onClick:()->Unit) {

    Box(modifier = Modifier
        .wrapContentSize()
        .layoutId(AuthenticationConstraintsItem.ClickableTextSection.layoutId)){

        ClickableText(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)){
                append(text = "Don't have an account?" )
            }
            withStyle(style = SpanStyle(color = Blue600, fontWeight = FontWeight.SemiBold,fontSize = 14.sp)){
                append(" ")
                append(text = "Sign up")
            }
        }, onClick = { onClick() })
    }

}

