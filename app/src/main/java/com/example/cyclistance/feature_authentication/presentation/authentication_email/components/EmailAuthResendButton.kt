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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun EmailAuthResendButton(text: String, isEnabled: Boolean, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .layoutId(AuthenticationConstraintsItem.ResendButton.layoutId)
            .wrapContentSize(),
        contentAlignment = Alignment.Center) {


        TextButton(
            enabled = isEnabled,
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.primary,
                disabledContentColor = Black440,
                disabledBackgroundColor = Color.Transparent)) {
            Text(
                text,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold)

        }
    }
}


