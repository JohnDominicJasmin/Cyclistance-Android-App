package com.example.cyclistance.feature_main_screen.presentation.mapping_confirm_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.cyclistance.R
import com.example.cyclistance.feature_authentication.presentation.common.ErrorMessage
import com.example.cyclistance.feature_main_screen.domain.model.ButtonDescriptionModel
import com.example.cyclistance.theme.Black440
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.flowlayout.FlowRow



@Composable
fun ButtonDescriptionDetails(
    modifier: Modifier,
    errorMessage: String,
    selectedOption: String,
    onClickButton: (String) -> Unit) {

    val hasError = errorMessage.isNotEmpty()
    val buttonOptions = listOf(
        ButtonDescriptionModel(buttonText = "Injury", icon = R.drawable.ic_injury),
        ButtonDescriptionModel(buttonText = "Frame Snap", icon = R.drawable.ic_broken_frame),
        ButtonDescriptionModel(buttonText = "Accident", icon = R.drawable.ic_accident),
        ButtonDescriptionModel(buttonText = "Chain Snap", icon = R.drawable.ic__670665_200),
        ButtonDescriptionModel(buttonText = "Flat tire", icon = R.drawable.ic_flat_tire),
        ButtonDescriptionModel(buttonText = "Faulty Brakes", icon = R.drawable.ic_faulty_brakes)
    )





    Column(modifier = modifier) {


        Text(
            text = "Description",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))


        FlowRow(
            mainAxisSpacing = 7.dp,
            crossAxisSpacing = 10.dp,
            modifier = Modifier.fillMaxWidth()) {

            buttonOptions.forEach {
                SetupButtonDescriptionItem(
                    selectedState = it.buttonText == selectedOption,
                    image = it.icon,
                    description = it.buttonText,
                    onClick = {
                        onClickButton(it.buttonText)
                    })
            }
        }

        if (hasError) {
            ErrorMessage(
                errorMessage = errorMessage,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 1.2.dp))
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
        val borderColor = if (selectedState) MaterialTheme.colors.primary else Color.Transparent

        OutlinedButton(
            contentPadding = PaddingValues(all = 13.5.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface,
                disabledBackgroundColor = MaterialTheme.colors.surface),
            onClick = onClick,
            border = BorderStroke(width = 2.dp, color = borderColor),
            modifier = Modifier
                .wrapContentWidth()
                .height(125.dp)
                .shadow(7.dp, shape = RoundedCornerShape(12.dp), clip = true)
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


                Icon(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(65.dp)
                        .padding(top = 5.dp, bottom = 9.dp, ),
                    tint = MaterialTheme.colors.onSurface
                )

                Text(
                    text = description,
                    color = Black440,
                    style = MaterialTheme.typography.subtitle2,
                    textAlign = TextAlign.Center,
                overflow = TextOverflow.Clip)

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


@Preview(device = Devices.PIXEL_4)
@Composable
fun ButtonDescriptionItem() {


    CyclistanceTheme(false) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {

            ButtonDescriptionDetails(
                modifier = Modifier.fillMaxWidth(0.9f),
                errorMessage = "Field cannot be left blank.",
                selectedOption = "",
                onClickButton = { selectedDescription ->

                })

        }
    }
}
