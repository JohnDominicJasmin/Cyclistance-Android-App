package com.example.cyclistance.feature_message.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun MessagingTextArea(
    modifier: Modifier = Modifier,
    message: String,
    onValueChange: (String) -> Unit,
    onClickSend: () -> Unit = {},
    onToggleExpand: () -> Unit = {},
    isExpanded: Boolean = false

) {

    val isMessageEmpty by remember(message) { derivedStateOf { message.isEmpty() } }


    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colors.background,
        elevation = 1.5.dp) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center) {

            IconButton(onClick = {
                if (isExpanded) {
                    onToggleExpand()
                }
            }, modifier = Modifier.weight(0.1f)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_fold_message),
                    contentDescription = "Fold Message", tint = MaterialTheme.colors.primary)
            }


            OutlinedTextField(
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource, isExpanded) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    if (!isExpanded) {
                                        onToggleExpand()
                                    }
                                }
                            }
                        }
                    },
                value = message,
                onValueChange = {
                    if (!isExpanded) {
                        onToggleExpand()
                    }
                    onValueChange(it)
                },
                placeholder = {
                    Text(
                        text = "Type a message",
                        color = Black500,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(horizontal = 3.dp))
                },
                modifier = Modifier
                    .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessHigh))
                    .wrapContentHeight()
                    .weight(0.8f),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    textColor = MaterialTheme.colors.onSurface,
                    cursorColor = MaterialTheme.colors.primary,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(16.dp),
                maxLines = if (isExpanded) 6 else 1,
                singleLine = false,
                keyboardOptions = KeyboardOptions(autoCorrect = false)
            )

            IconButton(
                onClick = onClickSend,
                enabled = isMessageEmpty.not(),
                modifier = Modifier.weight(0.1f)) {

                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send Message",
                    tint = if (isMessageEmpty) Black500 else MaterialTheme.colors.primary
                )
            }

        }

    }


}

@Preview
@Composable
fun PreviewMessagingTextAreaDark() {
    var value by remember { mutableStateOf("") }
    CyclistanceTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            MessagingTextArea(message = value, onValueChange = { value = it }, onClickSend = {})
        }
    }
}

@Preview
@Composable
fun PreviewMessagingTextAreaLight() {
    var value by remember { mutableStateOf("") }
    CyclistanceTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            MessagingTextArea(message = value, onValueChange = { value = it }, onClickSend = {})
        }
    }
}

