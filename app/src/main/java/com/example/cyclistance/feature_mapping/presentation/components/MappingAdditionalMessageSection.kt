package com.example.cyclistance.feature_mapping.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@Composable
fun SetupAdditionalMessageSection(modifier: Modifier) {
    Column(modifier = modifier) {


        Text(
            text = "Message",
            color = Color.White,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterVertically)) {


            var text by remember { mutableStateOf(TextFieldValue("")) }

            TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color(0xFF404040),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    disabledIndicatorColor = Color.Transparent

                ),
                placeholder = {
                    Text(
                        text = "(Optional) Leave a message",
                        color = Color(0xFFB7B7B7),
                        style = MaterialTheme.typography.body2)
                },
            )


        }
    }
}