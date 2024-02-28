package com.myapp.cyclistance.feature_mapping.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.Red710

@Composable
fun BadgeCount(modifier: Modifier = Modifier, count: String) {

    Surface(
        shape = CircleShape,
        color = Red710,
        contentColor = Color.White,
        modifier = modifier.wrapContentSize()) {

        Box(
            contentAlignment = Alignment.Center) {
            Text(
                modifier = Modifier.padding(all = 4.dp),
                text = count,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.caption.fontSize)
        }
    }

}
