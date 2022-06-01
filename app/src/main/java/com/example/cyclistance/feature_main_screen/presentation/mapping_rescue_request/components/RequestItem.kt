package com.example.cyclistance.feature_main_screen.presentation.mapping_rescue_request.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.R
import com.example.cyclistance.feature_main_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.theme.*

data class CardData(

    val name: String = "",
    val profileImage:String = "",
    val estimatedTimeTravel: String = "",
    val distance:String = "",
    val address:String = "",

    )


@Composable
fun RequestItem(modifier: Modifier, cardState: CardData) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        backgroundColor = MaterialTheme.colors.surface) {
        Column(
            modifier = Modifier.padding(top = 12.dp, bottom = 5.dp)) {

            Row(
                modifier = Modifier.padding(start = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)) {

                Image(
                    painter = painterResource(R.drawable.person_image),
                    contentDescription = "User Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                Text(textAlign = TextAlign.Start,
                    text = buildAnnotatedString {

                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold)) {
                            append("${cardState.name}\n")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Black450,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium)) {
                            append(cardState.distance)
                        }
                    })
                Spacer(modifier = Modifier.weight(weight = 0.3f))

                Text(textAlign = TextAlign.End,
                    modifier = Modifier.padding(end = 15.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold)) {
                            append("ETA\n")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Black450,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium)) {
                            append(cardState.estimatedTimeTravel)
                        }

                    })

            }



            Divider(
                color = Black450,
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.92f)
                    .padding(top = 12.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                thickness = (1.5).dp)


            Row(
                modifier = Modifier.padding(start = 15.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "Address Icon",
                    tint = Color.Unspecified,
                )
                Text(text = cardState.address, color = Color.White)
            }

            MappingButtonNavigation(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 7.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                negativeButtonText = "Decline",
                positiveButtonText = "Accept",
                onClickCancelButton = {

                },
                onClickConfirmButton = {

                })

        }
    }

}

@Preview
@Composable
fun RequestItemPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)) {
        RequestItem(
            modifier = Modifier
                .fillMaxWidth(fraction = 0.9f)
                .wrapContentHeight(),
            cardState = sampleCardState[0])
    }
}