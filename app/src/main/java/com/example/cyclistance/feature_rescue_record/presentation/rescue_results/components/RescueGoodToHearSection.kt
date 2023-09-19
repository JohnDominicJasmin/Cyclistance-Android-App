package com.example.cyclistance.feature_rescue_record.presentation.rescue_results.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RescueGoodToHearSection(modifier: Modifier = Modifier) {

    val isDarkTheme = IsDarkTheme.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = painterResource(id = if (isDarkTheme) R.drawable.ic_high_five_dark else R.drawable.ic_high_five_light),
            contentDescription = "Good to hear")


        Text(
            text = "That’s good to hear!",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview
@Composable
fun PreviewRescueGoodToHearSectionDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .wrapContentSize()
                    .padding(all = 20.dp)) {
                RescueGoodToHearSection()
            }
        }
    }
}

@Preview
@Composable
fun PreviewRescueGoodToHearSectionLight() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .wrapContentSize()
                    .padding(all = 20.dp)) {
                RescueGoodToHearSection()
            }
        }
    }
}


