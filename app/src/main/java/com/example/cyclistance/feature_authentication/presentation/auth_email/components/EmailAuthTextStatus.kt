package com.example.cyclistance.feature_authentication.presentation.auth_email.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TEXT_LABEL
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun EmailAuthTextStatus(email: String) {


    Column(
        modifier = Modifier.layoutId(TEXT_LABEL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        EmailStatus(text = "Confirm your email address", fontWeight = FontWeight.ExtraBold)

        EmailStatus(text = "We sent confirmation email to:", fontWeight = FontWeight.Normal)

        EmailStatus(text = email, fontWeight = FontWeight.ExtraBold)

        EmailStatus(
            text = "To complete your verification, check your email (don’t forget to check your Spam Folder) and click on the confirmation link to continue.",
            fontWeight = FontWeight.Normal,
            modifier = Modifier.fillMaxWidth(0.8f).padding(top = 4.dp),
            fontColor = Black500
        )

    }
}


@Composable
private fun EmailStatus(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight,
    fontColor: Color = MaterialTheme.colors.onBackground
) {

    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.body1.copy(
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp,
            fontWeight = fontWeight,
            color = fontColor,
            textAlign = TextAlign.Center
        )
    )

}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun EmailStatusPreviewDark() {
    CyclistanceTheme(darkTheme = true) {
        Surface(modifier = Modifier.wrapContentSize(), color = MaterialTheme.colors.background) {
            EmailAuthTextStatus("sampleEmail@gmail.com")
        }
    }
}

@Preview(device = "id:Galaxy Nexus")
@Composable
fun EmailStatusPreviewLight() {
    CyclistanceTheme(darkTheme = false) {
        Surface(modifier = Modifier.wrapContentSize(), color = MaterialTheme.colors.background) {
            EmailAuthTextStatus("sampleEmail@gmail.com")
        }
    }
}