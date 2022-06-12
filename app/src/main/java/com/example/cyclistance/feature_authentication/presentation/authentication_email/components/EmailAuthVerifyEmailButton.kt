package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun EmailAuthVerifyEmailButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(50.dp)
            .width(220.dp)
            .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true)
            .layoutId(AuthenticationConstraintsItem.VerifyEmailButton.layoutId),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(12.dp)) {
        Text(
            text = "Verify Email",
            color = MaterialTheme.colors.onPrimary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center

        )
    }
}

@Preview
@Composable
fun EmailAuthVerifyEmailButtonPreview() {
    CyclistanceTheme(true){
        EmailAuthVerifyEmailButton {
            
        }
    }

}