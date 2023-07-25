package com.example.cyclistance.feature_messaging.presentation.chat.chats.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AddMessageButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.background,
        modifier = modifier
            .padding(all = 12.dp),
        shape = CircleShape,
        onClick = onClick) {

        Icon(
            painter = painterResource(id = R.drawable.baseline_edit_24),
            contentDescription = "Add Message",
            modifier = Modifier
                .padding(12.dp)
                .size(28.dp),
        )
    }
}
