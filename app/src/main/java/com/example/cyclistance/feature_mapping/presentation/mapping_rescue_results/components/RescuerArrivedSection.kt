package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_results.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
fun RescueArrivedSection(modifier: Modifier = Modifier) {

    val isDarkTheme = IsDarkTheme.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = painterResource(id = if (isDarkTheme) R.drawable.ic_cape_dark else R.drawable.ic_cape_light),
            contentDescription = "Rescuer Arrived",
            modifier = Modifier.weight(0.8f)
        )

        Text(
            text = "Your rescuer has arrived",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground
        )

    }
}

@Preview
@Composable
fun PreviewRescueArrivedResultsDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .wrapContentSize()
                    .padding(all = 20.dp)) {
                RescueArrivedSection()
            }
        }
    }

}


@Preview
@Composable
fun PreviewRescueArrivedResultsLight() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .wrapContentSize()
                    .padding(all = 20.dp)) {
                RescueArrivedSection()
            }
        }
    }

}




