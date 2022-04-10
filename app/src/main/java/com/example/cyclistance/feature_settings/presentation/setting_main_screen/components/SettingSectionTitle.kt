package com.example.cyclistance.feature_settings.presentation.setting_main_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.EditProfileTextFieldIndicatorColor

@Composable
fun SectionTitle(@DrawableRes iconId: Int, title: String) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)) {

            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "Account Icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(18.dp)

            )

            Text(
                text = title,
                color = Color.White,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.weight(0.9f))
        }




        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 3.dp, end = 3.dp, top = 2.dp),
            color = EditProfileTextFieldIndicatorColor)
    }
}