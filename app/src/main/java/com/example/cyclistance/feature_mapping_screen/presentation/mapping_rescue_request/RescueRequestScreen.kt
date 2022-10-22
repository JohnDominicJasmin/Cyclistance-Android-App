package com.example.cyclistance.feature_mapping_screen.presentation.mapping_rescue_request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_mapping_screen.domain.model.CardModel
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_rescue_request.components.RequestItem
import com.example.cyclistance.theme.CyclistanceTheme

val sampleCardState = listOf(

     CardModel(

         id = "001",
         name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "6km away"),

     CardModel(
         id = "002",
         name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "2.9km away"),

     CardModel(
         id = "003",
         name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "2.9km away"),

     CardModel(
         id = "004",
         name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "1.9km away"),

     CardModel(
         id = "005",
         name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "6.2km away"),

     CardModel(
         id = "006",
         name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "10km away"),

     CardModel(
         id = "007",
         name = "John Doe",
         profileImage = "",
         estimatedTimeTravel = "5-7 Mins",
         address = "Manila, Philippines",
         distance = "2km away")

 )

@Composable
fun RescueRequestScreen(paddingValues: PaddingValues) {
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)) {

        val (items, titleText ) = createRefs()

        Text(text = "13 NEW REQUEST",
            color = MaterialTheme.colors.onBackground,
            style = TextStyle(letterSpacing = 4.sp, ),
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
                top.linkTo(titleText.bottom, margin = 50.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }) {

            items(items = sampleCardState, key = {it.id}){ requestItem ->
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
    CyclistanceTheme(true) {
        RescueRequestScreen(paddingValues = PaddingValues())
    }
}