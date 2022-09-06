package com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details.components

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
import com.example.cyclistance.feature_authentication.presentation.common.textFieldColors
import com.example.cyclistance.theme.Black500

@Composable
fun AddressTextField(modifier: Modifier, address: String, onValueChange: (String) -> Unit) {

    TextField(
        value = address,
        onValueChange = onValueChange,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        label = {
            Text(
                text = "Address",
                color = Black500,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.LocationCity,
                contentDescription = "Email Icon",
                tint = Black500
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = textFieldColors(),
    )
}
