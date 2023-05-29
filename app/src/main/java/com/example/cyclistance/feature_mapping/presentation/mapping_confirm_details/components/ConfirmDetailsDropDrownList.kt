package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.common.ErrorMessage
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

sealed class BikeType(
    val type: String,
) {
    object RoadBike : BikeType(type = "Road Bike")
    object MountainBike : BikeType(type = "Mountain Bike")
    object FatBike : BikeType(type = "Fat Bike")
    object TouringBike : BikeType(type = "Touring Bike")
    object FixedGear : BikeType(type = "Fixed Gear")
    object BMX : BikeType(type = "BMX")
    object JapaneseBike : BikeType(type = "Japanese Bike")

}


private val bikeList = listOf(
    BikeType.RoadBike,
    BikeType.MountainBike,
    BikeType.FatBike,
    BikeType.TouringBike,
    BikeType.FixedGear,
    BikeType.BMX,
    BikeType.JapaneseBike
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownBikeList(
    modifier: Modifier,
    errorMessage: String,
    selectedItem: String,
    enabled: Boolean,
    onClickItem: (String) -> Unit) {

    val hasError = errorMessage.isNotEmpty()
    var expanded by remember { mutableStateOf(false) }


    Column(modifier = modifier) {

        ExposedDropdownMenuBox(
            modifier = Modifier
                .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
            expanded = expanded,
            onExpandedChange = {
                if (enabled) {
                    expanded = !expanded
                }
            }) {

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                readOnly = true,
                value = selectedItem,
                onValueChange = { },
                placeholder = {
                    Text(
                        "Bike Type",
                        color = Black500,
                        fontSize = MaterialTheme.typography.subtitle2.fontSize,
                        textAlign = TextAlign.Center)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.PedalBike,
                        contentDescription = "Bike Icon",
                        tint = Black500,
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colors.onSecondary,
                    backgroundColor = MaterialTheme.colors.secondary,
                    focusedIndicatorColor = MaterialTheme.colors.secondary,
                    unfocusedIndicatorColor = MaterialTheme.colors.secondary,
                    cursorColor = MaterialTheme.colors.primary,
                    trailingIconColor = Black500

                ),
                textStyle = TextStyle(fontSize = MaterialTheme.typography.subtitle2.fontSize)
            )


            ExposedDropdownMenu(modifier = Modifier
                .background(MaterialTheme.colors.secondary)
                .fillMaxWidth()
                .wrapContentHeight(),
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }) {


                bikeList.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            onClickItem(selectionOption.type)
                            expanded = false
                        }, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = selectionOption.type,
                            color = MaterialTheme.colors.onSecondary,
                            style = MaterialTheme.typography.subtitle2)
                    }
                }
            }
        }
        if (hasError) {
            ErrorMessage(errorMessage = errorMessage, modifier = Modifier.padding(top = 7.dp))
        }
    }

}

@Preview
@Composable
fun DropDownListPreview() {
    CyclistanceTheme(false) {
        DropDownBikeList(modifier = Modifier
            .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true),
            selectedItem = "Mountain Bike",
            errorMessage = "Field cannot be left blank",
            enabled = true,
            onClickItem = {

            })
    }
}
