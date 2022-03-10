package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor
import com.example.cyclistance.feature_authentication.presentation.theme.DisabledColor

@Composable
fun EmailAuthResendButton(onClick: ()->Unit) {

    Box(
        modifier = Modifier
            .layoutId(AuthenticationConstraintsItem.ResendButton.layoutId)
            .wrapContentSize(),
        contentAlignment = Alignment.Center) {


        TextButton(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = ThemeColor,
                disabledContentColor = DisabledColor)) {
            Text(
                "Resend email",
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold)

        }
    }
}

