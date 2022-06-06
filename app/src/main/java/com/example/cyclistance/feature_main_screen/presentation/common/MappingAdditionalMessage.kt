package com.example.cyclistance.feature_main_screen.presentation.common

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
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.common.MappingConstants.MAX_CHARACTERS
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.Black500


@Composable
fun MappingAdditionalMessage(modifier: Modifier) {

    Column(modifier = modifier) {




        Text(
            text = "Message",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterVertically)) {

            ConstraintLayout(modifier = Modifier.wrapContentHeight()) {


                var text by remember { mutableStateOf(TextFieldValue("")) }
                val (textField, numberOfCharactersText) = createRefs()
                val numberOfCharacters = remember { mutableStateOf(0) }

                TextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true)
                        .constrainAs(textField) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)

                        },
                    value = text,
                    onValueChange = { newText ->
                        if (newText.text.length <= MAX_CHARACTERS) {
                            numberOfCharacters.value = newText.text.length
                            text = newText
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colors.onSecondary,
                        backgroundColor = MaterialTheme.colors.secondary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colors.primary,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "(Optional) Leave a message",
                            color = Black500,
                            style = MaterialTheme.typography.body2)
                    },
                )

                Text(
                    text = "${numberOfCharacters.value}/$MAX_CHARACTERS",
                    color = Black440,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .constrainAs(numberOfCharactersText) {
                            top.linkTo(textField.bottom)
                            end.linkTo(parent.end)
                        })
            }

        }
    }
}