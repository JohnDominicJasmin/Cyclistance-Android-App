package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.common.ErrorMessage
import com.example.cyclistance.theme.*


@Composable
fun TextFieldInputArea(modifier: Modifier) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        var value by remember { mutableStateOf(TextFieldValue("")) }


        TextFieldItem(
            label = "Full Name",
            errorMessage = "",
            value = value,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next)) {
            value = it
        }

        TextFieldItem(label = "Phone Number",
            errorMessage = "Error messsage fix this later.",
            value = value,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {

            })) {
            value = it
        }
    }
}

@Composable
fun TextFieldItem(
    label: String,
    errorMessage: String,
    value: TextFieldValue,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (TextFieldValue) -> Unit) {

    val hasError = errorMessage.isNotEmpty()

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = Black500,
            modifier = Modifier.padding(bottom = 5.dp))

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            singleLine = true,
            maxLines = 1,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = MaterialTheme.colors.onBackground
            ),
            cursorBrush = Brush.verticalGradient(
                0.00f to MaterialTheme.colors.onBackground,
                1.00f to MaterialTheme.colors.onBackground),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth(), color = Black450)

        if (hasError) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Error,
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier.size(12.dp),
                    contentDescription = "Icon error")
                ErrorMessage(errorMessage = errorMessage, modifier = Modifier.padding(1.2.dp))
            }
        }

    }
}

