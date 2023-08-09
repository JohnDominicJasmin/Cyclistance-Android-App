package com.example.cyclistance.feature_authentication.presentation.auth_email.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.theme.Black440


@Composable
fun EmailAuthResendButton(text: String, isEnabled: Boolean, onClickResendButton: () -> Unit) {

    Box(
        modifier = Modifier
            .layoutId(AuthenticationConstraintsItem.ResendButton.layoutId)
            .wrapContentSize(),
        contentAlignment = Alignment.Center) {


        TextButton(
            enabled = isEnabled,
            onClick = onClickResendButton,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                contentColor = MaterialTheme.colors.primary,
                disabledContentColor = Black440,
                disabledBackgroundColor = Color.Transparent)) {
            Text(
                text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.button.copy(
                    fontSize = TextUnit(
                        16f,
                        TextUnitType.Sp)))

        }
    }
}


