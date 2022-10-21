package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun MappingDefaultBanner(isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = expandVertically(),
        exit = shrinkVertically()) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface)) {

            Text(
                modifier = Modifier.padding(
                    top = 25.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 12.dp),
                text = "John Doe cancelled the rescue request. The reason is “Problem already fixed.\"",
                color = MaterialTheme.colors.onSurface, style = MaterialTheme.typography.body2)

            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.End)) {

                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { }) {

                    Text(
                        text = "DISMISS",
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.subtitle2)
                }

            }
            Divider()
        }
    }
}