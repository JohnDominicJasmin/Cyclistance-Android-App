package com.example.cyclistance.feature_settings.presentation.setting_main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R


@Composable
fun ButtonItem(buttonText: String) {
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(start = 3.dp)
            .wrapContentSize()
            .clickable {

            },
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = buttonText,
            color = Color.White,
            modifier = Modifier
                .weight(0.88f)
                .padding(top = 7.dp, bottom = 7.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "Arrow",
            tint = Color.Unspecified)

    }
}

