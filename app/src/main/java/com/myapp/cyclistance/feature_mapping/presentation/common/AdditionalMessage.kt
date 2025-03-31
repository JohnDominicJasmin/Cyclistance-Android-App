package com.myapp.cyclistance.feature_mapping.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.myapp.cyclistance.core.utils.constants.MappingConstants.CHARACTER_LIMIT
import com.myapp.cyclistance.navigation.IsDarkTheme
import com.myapp.cyclistance.theme.Black440
import com.myapp.cyclistance.theme.Black500


@Composable
fun AdditionalMessage(
    text: String? = "Message",
    placeholderText: String? = "(Optional) Leave a message",
    modifier: Modifier,
    message: TextFieldValue,
    enabled: Boolean,
    onChangeValueMessage: (TextFieldValue) -> Unit) {

    val isDarkTheme = IsDarkTheme.current

    Column(modifier = modifier) {


        if (text != null) {
            Text(
                text = text,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))
        }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterVertically)) {

            ConstraintLayout(modifier = Modifier.wrapContentHeight()) {


                val (textField, numberOfCharactersText) = createRefs()
                var numberOfCharacters by rememberSaveable { mutableIntStateOf(0) }

                TextField(
                    enabled = enabled,
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(
                            elevation = if (isDarkTheme) 0.dp else 2.5.dp,
                            shape = RoundedCornerShape(12.dp),
                            clip = true)
                        .constrainAs(textField) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)

                        },
                    value = message,
                    onValueChange = { newText ->
                        if (newText.text.length <= CHARACTER_LIMIT) {
                            numberOfCharacters = newText.text.length
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
                        placeholderText?.let {
                            Text(
                                text = placeholderText,
                                color = Black500,
                                style = MaterialTheme.typography.body2)
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = TextUnit(value = 14f, type = TextUnitType.Sp)
                    )
                )

                Text(
                    text = "${numberOfCharacters}/$CHARACTER_LIMIT",
                    color = Black440,
                    style = MaterialTheme.typography.caption,
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