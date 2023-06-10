package com.example.cyclistance.feature_intro_slider.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.theme.Black440
import com.google.accompanist.pager.*


private val introSliderItems  =
    listOf(
        IntroSliderScreenItems.LiveLocation,
        IntroSliderScreenItems.HelpAndRescue,
        IntroSliderScreenItems.RealTimeMessaging
    )


@ExperimentalPagerApi
@Composable
fun IntroSliderItem(
    pagerState: PagerState,
    modifier: Modifier = Modifier) {

    Surface(color = MaterialTheme.colors.background, modifier = modifier) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,) {

            HorizontalPager(
                modifier = Modifier.weight(0.7f),
                count = introSliderItems.size,
                state = pagerState) { page ->


                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,) {

                    Spacer(modifier = Modifier.weight(0.2f))

                    Image(
                        painter = painterResource(id = introSliderItems[page].image),
                        contentDescription = introSliderItems[page].title + " Images",
                        alignment = Alignment.Center,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 12.dp)
                            .weight(1.35f, fill = true),
                    )


                    Text(
                        modifier = Modifier
                            .padding(all = 10.dp)
                            .weight(0.4f, fill = false),
                        text = introSliderItems[page].title,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onBackground)

                    Text(
                        text = introSliderItems[page].description,
                        style = MaterialTheme.typography.body2.copy(
                            lineHeight = 14.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onBackground,
                        ),
                        modifier = Modifier
                            .fillMaxWidth(0.76f)
                            .weight(0.4f)
                    )
                    Spacer(modifier = Modifier.weight(0.2f))


                }
            }

            PagerIndicator(
                pagerState = pagerState, modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp))
        }
    }



}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagerIndicator(pagerState: PagerState,modifier: Modifier) {
    HorizontalPagerIndicator(
        pagerState = pagerState,
        modifier = modifier,
        activeColor = MaterialTheme.colors.primary,
        inactiveColor = Black440,
        indicatorWidth = 16.dp,
        indicatorHeight = 7.dp,
        spacing = 7.dp,
        indicatorShape = RoundedCornerShape(15.dp)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Preview(device = "id:Galaxy Nexus")
@Composable
fun IntroSliderItemPreview() {
    IntroSliderItem(PagerState(currentPage = 0))
}


