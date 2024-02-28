package com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueThankYouSection(modifier: Modifier = Modifier) {

    val isDarkTheme = IsDarkTheme.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = painterResource(id = if (isDarkTheme) R.drawable.ic_baloon_heart_dark else R.drawable.ic_baloon_heart_light),
            contentDescription = "Thank you",
            modifier = Modifier
                .padding(all = 12.dp)
                .weight(0.25f, fill = false)
        )

        Text(
            text = "Thank you!",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.weight(0.2f, fill = false)
        )

        Text(
            text = "You helped other bikers to decide on their next rescue assistance",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth(0.75f)
                .weight(0.2f, fill = false))
    }
}

@Preview
@Composable
fun PreviewRescueThankYouSection() {
    CyclistanceTheme(darkTheme = true) {
        RescueThankYouSection()
    }
}