package com.myapp.cyclistance.core.presentation.text_fields

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.feature_authentication.presentation.common.ErrorMessage
import com.myapp.cyclistance.theme.Black450
import com.myapp.cyclistance.theme.Black500

@Composable
fun TextFieldCreator(
    modifier: Modifier = Modifier,
    errorMessage: String = "",
    label: String,
    isOptional: Boolean = false,
    content: @Composable () -> Unit) {
    val hasError by remember(errorMessage) { derivedStateOf { errorMessage.isNotEmpty() } }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {

        Row(
            modifier = Modifier.padding(bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {

            Text(
                text = label,
                style = MaterialTheme.typography.caption,
                color = Black500,
                modifier = Modifier)

            if (isOptional) {
                Text(
                    text = "optional",
                    modifier = Modifier
                        .padding(all = 1.5.dp).padding(start = 4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colors.secondaryVariant)
                        .padding(all = 1.dp),
                    style = MaterialTheme.typography.overline,
                    color = MaterialTheme.colors.onSecondary
                )
            }

        }

        content()

        Divider(
            modifier = Modifier
                .fillMaxWidth(), color = Black450)

        if (hasError) {
            ErrorMessage(
                errorMessage = errorMessage,
                modifier = Modifier.padding(1.2.dp))
        }

    }
}

@Composable
fun TextFieldItem(
    value: TextFieldValue,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (TextFieldValue) -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit = {}) {


    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        enabled = enabled,
        value = value,
        singleLine = true,
        maxLines = 1,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = MaterialTheme.colors.onBackground,
            fontSize = MaterialTheme.typography.subtitle2.fontSize,
        ),
        cursorBrush = Brush.verticalGradient(
            0.00f to MaterialTheme.colors.onBackground,
            1.00f to MaterialTheme.colors.onBackground),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )


}
