package com.example.cyclistance.feature_mapping_screen.presentation.mapping_rescue_request.components

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping_screen.domain.model.CardModel
import com.example.cyclistance.feature_mapping_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.theme.Black450


@Composable
fun RequestItem(
    modifier: Modifier,
    cardState: CardModel,
    onClickCancelButton: () -> Unit,
    onClickConfirmButton: () -> Unit) {

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

                AsyncImage(
                    model = cardState.profileImageUrl,
                    contentDescription = "User Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape))

                Text(textAlign = TextAlign.Start,
                    text = buildAnnotatedString {

                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold)) {
                            append("${cardState.name}\n")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Black450,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium)) {
                            append(cardState.distance ?: "----")
                        }
                    })
                Spacer(modifier = Modifier.weight(weight = 0.3f))

                Text(textAlign = TextAlign.End,
                    modifier = Modifier.padding(end = 15.dp),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colors.onSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold)) {
                            append("ETA\n")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Black450,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium)) {
                            append(cardState.estimatedTimeTravel ?: "----")
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
                Text(text = cardState.address ?: "----", color = MaterialTheme.colors.onSurface)
            }

            MappingButtonNavigation(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 7.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                negativeButtonText = "Decline",
                positiveButtonText = "Accept",
                onClickCancelButton = onClickCancelButton,
                onClickConfirmButton = onClickConfirmButton)

        }
    }

}

/*
@Preview
@Composable
fun RequestItemPreview() {

    CyclistanceTheme(true) {

            RequestItem(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.9f)
                    .wrapContentHeight(),
                cardState = Card)
        }
}
*/
