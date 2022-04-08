package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.theme.BackgroundColor
import com.example.cyclistance.theme.DisabledColor
import com.example.cyclistance.theme.ThemeColor


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
            colors = ButtonDefaults.buttonColors(
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
