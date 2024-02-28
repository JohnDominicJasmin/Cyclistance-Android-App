package com.myapp.cyclistance.feature_authentication.presentation.auth_email.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.VERIFY_EMAIL_BUTTON_ID
import com.myapp.cyclistance.theme.CyclistanceTheme


@Composable
fun EmailAuthVerifyEmailButton(enabled: Boolean = false, onClickVerifyButton: () -> Unit) {
    Button(
        onClick = onClickVerifyButton,
        enabled = enabled,
        modifier = Modifier
            .height(50.dp)
            .widthIn(max = 650.dp)
            .fillMaxWidth(0.73f)
            .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true)
            .layoutId(VERIFY_EMAIL_BUTTON_ID),

        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            disabledBackgroundColor = MaterialTheme.colors.primary,
            disabledContentColor = MaterialTheme.colors.onPrimary),

        shape = RoundedCornerShape(12.dp)) {
        Text(
            text = "Verify Email",
            style = MaterialTheme.typography.button.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            ),


            )
    }
}

@Preview(name = "Email Verify Dark Theme")
@Composable
fun PreviewVerifyEmailDark() {
    CyclistanceTheme(true) {
        EmailAuthVerifyEmailButton(onClickVerifyButton = {})
    }
}

@Preview(name = "Email Verify Light Theme")
@Composable
fun PreviewVerifyEmailLight() {
    CyclistanceTheme(false) {
        EmailAuthVerifyEmailButton(onClickVerifyButton = {})
    }
}


