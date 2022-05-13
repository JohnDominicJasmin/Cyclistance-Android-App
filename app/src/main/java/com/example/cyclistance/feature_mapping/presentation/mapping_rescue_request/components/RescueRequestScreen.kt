package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.CardState
import com.example.cyclistance.theme.BackgroundColor

 val sampleCardState = listOf(

     CardState(name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "6.9km away"),

     CardState(name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "6.9km away"),

     CardState(name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "6.9km away"),

     CardState(name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "6.9km away"),
     CardState(name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "6.9km away"),
     CardState(name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "6.9km away"),
     CardState(name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "6.9km away")

 )
@Composable
fun RescueRequestScreen() {
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(BackgroundColor)) {

        val (items, titleText ) = createRefs()

        Text(text = "3 NEW REQUEST",
            color = Color.White,
            style = TextStyle(
                letterSpacing = 4.sp,
            ),
            modifier = Modifier.constrainAs(titleText){
                top.linkTo(parent.top, margin = 14.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                width = Dimension.wrapContent
            },
            textAlign = TextAlign.Center

        )


        LazyColumn(modifier = Modifier
            .constrainAs(items) {
                top.linkTo(titleText.bottom, margin = 12.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                height = Dimension.percent(0.9f)
            }) {

            items(items = sampleCardState){ requestItem ->
                RequestItem(
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.9f)
                        .padding(start = 4.dp, end = 4.dp, top = 6.dp, bottom = 6.dp)
                        .wrapContentHeight(), cardState = requestItem)
            }

        }
    }


}



@Preview
@Composable
fun PreviewRescueRequest() {
    RescueRequestScreen()
}