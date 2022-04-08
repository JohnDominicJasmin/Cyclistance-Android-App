package com.example.cyclistance.feature_mapping.presentation.mapping_edit_profile.components

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
import com.example.cyclistance.theme.TextFieldTextHintColor
import com.example.cyclistance.theme.editProfileTextFieldIndicatorColor
import com.example.cyclistance.theme.errorColor


@Composable
fun TextFieldInputArea(modifier: Modifier) {
    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        var value by remember { mutableStateOf(TextFieldValue("")) }


        TextFieldInputItem(
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

        TextFieldInputItem(
            label = "E-mail",
            errorMessage = "",
            value = value,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next)) {
            value = it
        }

        TextFieldInputItem(
            label = "Address",
            errorMessage = "",
            value = value,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next)) {
            value = it
        }

        TextFieldInputItem(label = "Phone Number",
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
fun TextFieldInputItem(
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
            color = TextFieldTextHintColor,
            modifier = Modifier.padding(bottom = 5.dp))

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = Color.White
            ),
            cursorBrush = Brush.verticalGradient(
                0.00f to Color.White,
                1.00f to Color.White),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth(), color = editProfileTextFieldIndicatorColor)

        if (hasError) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Error,
                    tint = errorColor,
                    modifier = Modifier.size(12.dp),
                    contentDescription = "Icon error")
                ErrorMessage(errorMessage = errorMessage, modifier = Modifier.padding(1.2.dp))
            }
        }

    }
}

