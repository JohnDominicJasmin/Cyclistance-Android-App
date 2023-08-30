package com.example.cyclistance.feature_authentication.presentation.auth_sign_up.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.SIGN_UP_BUTTON_ID


@Composable
fun SignUpButton(enabled: Boolean, onClickSignUpButton: () -> Unit) {


    Box(
        modifier = Modifier
            .layoutId(layoutId = SIGN_UP_BUTTON_ID)
            .wrapContentSize()
            .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
        contentAlignment = Alignment.Center) {


        Button(
            enabled = enabled,
            onClick = onClickSignUpButton,
            modifier = Modifier
                .height(45.dp)
                .width(220.dp)
                .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                disabledContentColor = MaterialTheme.colors.onPrimary,
                disabledBackgroundColor = MaterialTheme.colors.primary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Sign Up", style = MaterialTheme.typography.button)
        }
    }

}