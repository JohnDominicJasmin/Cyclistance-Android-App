package com.example.cyclistance.feature_mapping.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.example.cyclistance.feature_authentication.presentation.theme.BannerBackgroundColor
import com.example.cyclistance.feature_authentication.presentation.theme.ThemeColor

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun PrevBanner() {
    val isVisible = remember { mutableStateOf(false) }


        MappingBanner(isVisible)



}

@ExperimentalAnimationApi
@Composable
fun MappingBanner(isVisible: MutableState<Boolean>) {
    AnimatedVisibility(
        visible = isVisible.value,
        enter = expandVertically(),
        exit = shrinkVertically()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BannerBackgroundColor)) {

            Text(
                modifier = Modifier.padding(
                    top = 25.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp),
                text = "John Doe cancelled the rescue request. The reason is “Problem already fixed.",
                color = Color.White, style = MaterialTheme.typography.body2)

            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.End)) {

                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { isVisible.value = false }) {

                    Text(
                        text = "MESSAGE",
                        color = ThemeColor,
                        style = MaterialTheme.typography.subtitle2)
                }

                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { isVisible.value = false }) {
                    Text(
                        text = "DISMISS",
                        color = ThemeColor,
                        style = MaterialTheme.typography.subtitle2)
                }
            }
            Divider()
        }
    }
}
