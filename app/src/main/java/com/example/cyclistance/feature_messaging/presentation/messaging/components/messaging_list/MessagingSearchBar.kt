package com.example.cyclistance.feature_messaging.presentation.messaging.components.messaging_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun MessagingSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onClickClearSearch: () -> Unit) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.shadow(
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp),
            clip = true),
        placeholder = {
            Text(
                text = "Search",
                style = MaterialTheme.typography.body2,
                color = Black500,
                modifier = Modifier.padding(horizontal = 3.dp))
        }, leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Black500
            )
        }, trailingIcon = {
            AnimatedVisibility(
                visible = value.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(durationMillis = 100)),
                exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

                IconButton(onClick = onClickClearSearch) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Search"
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(autoCorrect = false),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onSecondary,
            backgroundColor = MaterialTheme.colors.secondary,
            cursorColor = MaterialTheme.colors.primary,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        )

    )
}

@Preview(name = "SearchField Dark theme", device = "id:Galaxy Nexus")
@Composable
fun PreviewMessagingSearchBarDark() {

    var value by remember { mutableStateOf("") }

    CyclistanceTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp),
            contentAlignment = Alignment.Center) {
            MessagingSearchBar(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(all = 4.dp), onClickClearSearch = {
                    value = ""
                })
        }
    }
}


@Preview(name = "SearchField Light theme", device = "id:Galaxy Nexus")
@Composable
fun PreviewMessagingSearchBarLight() {

    var value by remember { mutableStateOf("") }

    CyclistanceTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp),
            contentAlignment = Alignment.Center) {
            MessagingSearchBar(
                value = value,
                onValueChange = { value = it },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(all = 4.dp), onClickClearSearch = {
                    value = ""
                })
        }
    }
}

