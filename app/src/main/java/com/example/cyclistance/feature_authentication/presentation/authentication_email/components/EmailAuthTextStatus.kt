package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem

@Composable
fun EmailAuthTextStatus(email: String) {


    Column(
        modifier = Modifier.layoutId(AuthenticationConstraintsItem.WelcomeTextArea.layoutId),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)) {

        EmailStatus(text = "Confirm your email address", fontWeight = FontWeight.ExtraBold)

        EmailStatus(text = "We sent confirmation email to:", fontWeight = FontWeight.Normal)

        EmailStatus(text = email, fontWeight = FontWeight.ExtraBold)

        EmailStatus(
            text = "Check your email and click on the \n" +
                   " confirmation link to continue.",
            fontWeight = FontWeight.Normal)
        
    }
}


@Composable
private fun EmailStatus(modifier: Modifier = Modifier, text: String, fontWeight: FontWeight) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colors.onBackground,
        fontSize = 16.sp,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center)

}

@Preview
@Composable
fun EmailStatusPreview() {
    EmailAuthTextStatus("sampleEmail@gmail.com")
}