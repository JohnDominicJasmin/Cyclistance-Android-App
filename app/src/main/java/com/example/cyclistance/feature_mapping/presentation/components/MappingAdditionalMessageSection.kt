package com.example.cyclistance.feature_mapping.presentation.components

import androidx.compose.foundation.layout.*
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
fun SetupAdditionalMessageSection(modifier: Modifier ) {
    Column(modifier = modifier) {


        Text(
            text = "Message",
            color = Color.White,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))

        Box(modifier = Modifier.wrapContentHeight()) {


            var text by remember { mutableStateOf(TextFieldValue("")) }
            val numberOfCharacters = remember { mutableStateOf(0)}
            val maxCharacter = 256

            TextField(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
                value = text,
                onValueChange = { newText ->
                    if(newText.text.length <= maxCharacter) {
                        numberOfCharacters.value = newText.text.length
                        text = newText
                    }
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
            Text(
                text = "Message",
                color = Color.White,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))


        }
    }
}