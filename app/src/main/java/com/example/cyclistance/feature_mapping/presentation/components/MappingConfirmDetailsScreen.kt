package com.example.cyclistance.feature_mapping.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.PedalBike
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.common.TextFieldColors
import com.example.cyclistance.feature_authentication.presentation.theme.*


private val bikeList = listOf(
    "Road Bike",
    "Mountain Bike",
    "Fat Bike",
    "Touring Bike",
    "Fixed Gear/ Track Bike",
    "BMX",
    "Japanese Bike")


@Preview
@Composable
fun MappingDetailsDialogScreen() {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundColor)) {

        val (addressTextField, bikeTypeDropDownList, buttonDescriptionSection, additionalMessageSection, buttonNavButtonSection) = createRefs()



        AddressTextField(
            modifier = Modifier
                .shadow(2.dp, shape = RoundedCornerShape(12.dp), clip = true)
                .constrainAs(addressTextField) {
                    top.linkTo(parent.top, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    width = Dimension.percent(0.9f)
                    height = Dimension.wrapContent
                })


        DropDownBikeList(modifier = Modifier
            .shadow(2.dp, shape = RoundedCornerShape(12.dp), clip = true)
            .constrainAs(bikeTypeDropDownList) {
                top.linkTo(addressTextField.bottom, margin = 10.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                width = Dimension.percent(0.9f)
                height = Dimension.wrapContent
            })

        ButtonDescriptionDetails(
            modifier = Modifier
                .wrapContentHeight()
                .constrainAs(buttonDescriptionSection) {
                    top.linkTo(bikeTypeDropDownList.bottom, margin = 5.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.wrapContent
                    width = Dimension.percent(0.9f)
                })

        SetupAdditionalMessageSection(
            modifier = Modifier
                .constrainAs(additionalMessageSection) {
                    top.linkTo(buttonDescriptionSection.bottom, margin = 15.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.percent(0.25f)
                    width = Dimension.percent(0.9f)

                }
        )

        ButtonDialogSection(
            modifier = Modifier
                .constrainAs(buttonNavButtonSection) {
                    top.linkTo(additionalMessageSection.bottom, margin = 15.dp)
                    bottom.linkTo(parent.bottom, margin = 2.dp)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    height = Dimension.wrapContent
                    width = Dimension.percent(0.9f)
                },
            onClickCancelButton = {

            },
            onClickConfirmButton = {

            })


    }


}

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
                color = TextFieldTextColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.LocationCity,
                contentDescription = "Email Icon",
                tint = TextFieldTextColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldColors(),
    )
}


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
                    color = TextFieldTextColor,
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
                    tint = TextFieldTextColor,
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = TextFieldBackgroundColor,
                focusedIndicatorColor = TextFieldBackgroundColor,
                unfocusedIndicatorColor = TextFieldBackgroundColor,
                cursorColor = Color.White,
                trailingIconColor = TextFieldTextColor

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

@Composable
fun ButtonDescriptionDetails(modifier: Modifier) {
    val selectedButtonState =
        remember { mutableStateListOf(false, false, false, false, false, false) }





    Column(modifier = modifier) {


        Text(
            text = "Description",
            color = Color.White,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 5.dp, bottom = 2.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                10.dp,
                alignment = Alignment.CenterVertically)) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    5.dp,
                    alignment = Alignment.CenterHorizontally)) {

                SetupButtonDescriptionItem(
                    selectedState = selectedButtonState[0],
                    image = R.drawable.ic_injury,
                    description = "Injury") {
                    selectedButtonState[0] = !selectedButtonState[0]
                    selectedButtonState[1] = false
                    selectedButtonState[2] = false
                    selectedButtonState[3] = false
                    selectedButtonState[4] = false
                    selectedButtonState[5] = false

                }

                SetupButtonDescriptionItem(
                    selectedState = selectedButtonState[1],
                    image = R.drawable.ic_broken_frame,
                    description = "Broken Frame") {
                    selectedButtonState[1] = !selectedButtonState[1]
                    selectedButtonState[0] = false
                    selectedButtonState[2] = false
                    selectedButtonState[3] = false
                    selectedButtonState[4] = false
                    selectedButtonState[5] = false

                }

                SetupButtonDescriptionItem(
                    selectedState = selectedButtonState[2],
                    image = R.drawable.ic_accident,
                    description = "Accident") {
                    selectedButtonState[2] = !selectedButtonState[2]
                    selectedButtonState[0] = false
                    selectedButtonState[1] = false
                    selectedButtonState[3] = false
                    selectedButtonState[4] = false
                    selectedButtonState[5] = false

                }


            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    5.dp,
                    alignment = Alignment.CenterHorizontally)) {


                SetupButtonDescriptionItem(
                    selectedState = selectedButtonState[3],
                    image = R.drawable.ic__670665_200,
                    description = "Snapped \nChain") {
                    selectedButtonState[3] = !selectedButtonState[3]
                    selectedButtonState[0] = false
                    selectedButtonState[1] = false
                    selectedButtonState[2] = false
                    selectedButtonState[4] = false
                    selectedButtonState[5] = false
                }

                SetupButtonDescriptionItem(
                    selectedState = selectedButtonState[4],
                    image = R.drawable.ic_flat_tire,
                    description = "Flat tire") {
                    selectedButtonState[4] = !selectedButtonState[4]
                    selectedButtonState[0] = false
                    selectedButtonState[1] = false
                    selectedButtonState[2] = false
                    selectedButtonState[3] = false
                    selectedButtonState[5] = false
                }

                SetupButtonDescriptionItem(
                    selectedState = selectedButtonState[5],
                    image = R.drawable.ic_broken_brakes,
                    description = "Broken \nBrakes") {
                    selectedButtonState[5] = !selectedButtonState[5]
                    selectedButtonState[0] = false
                    selectedButtonState[1] = false
                    selectedButtonState[2] = false
                    selectedButtonState[3] = false
                    selectedButtonState[4] = false
                }


            }

        }
    }
}

@Composable
fun SetupButtonDescriptionItem(
    selectedState: Boolean,
    image: Int,
    description: String,
    onClick: () -> Unit) {

    ConstraintLayout(modifier = Modifier.wrapContentSize()) {


        val (checkIcon, buttonItem) = createRefs()
        val borderColor = if (selectedState) ThemeColor else DisabledColor

        OutlinedButton(
            contentPadding = PaddingValues(start = 13.dp, end = 13.dp),
            colors = buttonColors(
                backgroundColor = BackgroundColor,
                disabledBackgroundColor = BackgroundColor),
            onClick = onClick,
            border = BorderStroke(width = 2.dp, color = borderColor),
            modifier = Modifier
                .wrapContentWidth()
                .height(130.dp)
                .shadow(7.dp, shape = RoundedCornerShape(15.dp), clip = true)
                .constrainAs(buttonItem) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                },
            shape = RoundedCornerShape(12.dp)) {


            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {


                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(75.dp)
                        .padding(top = 5.dp, bottom = 9.dp),
                )

                Text(
                    text = description,
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Center)

            }
        }

        if (selectedState) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check_icon),
                contentDescription = "null",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(15.dp)
                    .constrainAs(checkIcon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    })
        }
    }


}



