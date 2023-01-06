package com.example.cyclistance.feature_mapping.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.core.utils.constants.MappingConstants.CHARACTER_LIMIT
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.Black500


@Composable
fun AdditionalMessage(
    text: String = "Message",
    modifier: Modifier,
    message: String,
    enabled: Boolean,
    onChangeValueMessage: (String) -> Unit) {
    Column(modifier = modifier) {




        Text(
            text = text,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterVertically)) {

            ConstraintLayout(modifier = Modifier.wrapContentHeight()) {


                val (textField, numberOfCharactersText) = createRefs()
                val numberOfCharacters = remember { mutableStateOf(0) }

                TextField(
                    enabled = enabled,
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true)
                        .constrainAs(textField) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)

                        },
                    value = message,
                    onValueChange = { newText ->
                        if (newText.length <= CHARACTER_LIMIT) {
                            numberOfCharacters.value = newText.length
                            onChangeValueMessage(newText)
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
                    text = "${numberOfCharacters.value}/$CHARACTER_LIMIT",
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