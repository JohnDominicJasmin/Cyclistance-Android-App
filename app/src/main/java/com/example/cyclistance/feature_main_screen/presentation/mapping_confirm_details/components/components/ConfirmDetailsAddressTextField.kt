package com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details.components.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.common.TextFieldColors
import com.example.cyclistance.theme.TextFieldTextHintColor

@Composable
fun AddressTextField(modifier: Modifier) {

    var address by remember { mutableStateOf("Manila Philippines") }

    TextField(
        value = address,
        onValueChange = { address = it },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        label = {
            Text(
                text = "Address",
                color = TextFieldTextHintColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.LocationCity,
                contentDescription = "Email Icon",
                tint = TextFieldTextHintColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldColors(),
    )
}
