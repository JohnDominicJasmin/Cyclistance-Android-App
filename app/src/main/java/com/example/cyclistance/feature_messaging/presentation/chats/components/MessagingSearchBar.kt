package com.example.cyclistance.feature_messaging.presentation.chats.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun MessagingSearchBar(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit) {


    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = {
            Text(
                text = "Search",
                style = MaterialTheme.typography.body1,
                color = Black500,
                modifier = Modifier.padding(horizontal = 3.dp))
        },
        keyboardOptions = KeyboardOptions(autoCorrect = false),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colors.onSecondary,
            backgroundColor = Color.Transparent,
            cursorColor = MaterialTheme.colors.primary,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        ),
        maxLines = 1,
        textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onBackground)
    )
}

@Preview(name = "SearchField Dark theme", device = "id:Galaxy Nexus")
@Composable
fun PreviewMessagingSearchBarDark() {

    var value by remember { mutableStateOf(TextFieldValue()) }

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
                    .padding(all = 4.dp))
        }
    }
}


@Preview(name = "SearchField Light theme", device = "id:Galaxy Nexus")
@Composable
fun PreviewMessagingSearchBarLight() {

    var value by remember { mutableStateOf(TextFieldValue()) }

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
                    .padding(all = 4.dp))
        }
    }
}

