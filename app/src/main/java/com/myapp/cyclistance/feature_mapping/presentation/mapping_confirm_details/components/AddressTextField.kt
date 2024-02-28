package com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.feature_authentication.presentation.common.ErrorMessage
import com.myapp.cyclistance.feature_authentication.presentation.common.textFieldColors
import com.myapp.cyclistance.theme.Black500
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun AddressTextField(
    modifier: Modifier = Modifier,
    address: TextFieldValue,
    addressErrorMessage: String,
    enabled: Boolean = true,
    onValueChange: (TextFieldValue) -> Unit) {

    val hasError = addressErrorMessage.isNotEmpty()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(3.dp)) {

        TextField(
            enabled = enabled,
            value = address,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(10.dp, shape = RoundedCornerShape(12.dp), clip = true),
            shape = RoundedCornerShape(12.dp),
            maxLines = 1,
            label = {
                Text(
                    text = "Address",
                    color = Black500,
                    style = MaterialTheme.typography.caption,
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
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true
        )

        if(hasError) {

            ErrorMessage(
                errorMessage = addressErrorMessage,
            )
        }
    }
}


@Preview(device = "id:Galaxy Nexus")
@Composable
fun AddressTextFieldPreview() {
    CyclistanceTheme(true) {
        AddressTextField(
            address = TextFieldValue("Tanauan City Batangas"),
            addressErrorMessage = "Invalid Address",
            onValueChange = {})
    }
}