package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.theme.BackgroundColor
import com.example.cyclistance.theme.TextFieldBackgroundColor
import com.example.cyclistance.theme.TextFieldTextHintColor

private val bikeList = listOf(
    "Road Bike",
    "Mountain Bike",
    "Fat Bike",
    "Touring Bike",
    "Fixed Gear/ Track Bike",
    "BMX",
    "Japanese Bike")

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownBikeList(modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }


    var selectedOptionText by remember { mutableStateOf("") }


    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }) {

        TextField(
            modifier = Modifier
                .shadow(15.dp, shape = RoundedCornerShape(12.dp), clip = true)
                .fillMaxWidth()
                .wrapContentHeight(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            placeholder = {
                Text(
                    "Bike Type",
                    color = TextFieldTextHintColor,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center)
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.PedalBike,
                    contentDescription = "Bike Icon",
                    tint = TextFieldTextHintColor,
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = TextFieldBackgroundColor,
                focusedIndicatorColor = TextFieldBackgroundColor,
                unfocusedIndicatorColor = TextFieldBackgroundColor,
                cursorColor = Color.White,
                trailingIconColor = TextFieldTextHintColor

            ),
        )


        ExposedDropdownMenu(modifier = Modifier
            .background(BackgroundColor)
            .fillMaxWidth()
            .wrapContentHeight(),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }) {


            bikeList.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    }, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = selectionOption,
                        color = Color.White,
                        style = MaterialTheme.typography.subtitle2)
                }
            }
        }
    }
}
