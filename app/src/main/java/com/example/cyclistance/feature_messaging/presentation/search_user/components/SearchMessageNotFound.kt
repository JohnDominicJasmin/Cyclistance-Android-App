package com.example.cyclistance.feature_messaging.presentation.search_user.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
fun SearchMessageNotFound(modifier: Modifier = Modifier) {

    val isDarkTheme = IsDarkTheme.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {
        val image =
            if (isDarkTheme) R.drawable.ic_messaging_searching_not_found_dark else R.drawable.ic_messaging_searching_not_found_light

        Image(
            painter = painterResource(image),
            contentDescription = "Search Message Not Found",
            modifier = Modifier.padding(all = 4.dp)
        )

        Text(
            text = "No results",
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onBackground,
        )


    }
}

@Preview
@Composable
private fun PreviewSearchMessageNotFoundDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(darkTheme = true) {
            Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
                SearchMessageNotFound()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSearchMessageNotFoundLight() {
    CompositionLocalProvider(IsDarkTheme provides false) {
        CyclistanceTheme(darkTheme = false) {
            Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
                SearchMessageNotFound()
            }
        }
    }
}


