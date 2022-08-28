package com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.theme.Black440
import com.google.accompanist.pager.*


private val introSliderItems  =
    listOf(
        IntroSliderScreenItem.LiveLocation,
        IntroSliderScreenItem.HelpAndRescue,
        IntroSliderScreenItem.RealTimeMessaging
    )





@ExperimentalPagerApi
@Composable
fun IntroSliderItem(
    pagerState: PagerState) {


            Column(
            modifier = Modifier.layoutId(IntroSliderConstraintsItem.HorizontalPager.layoutId)) {

            HorizontalPager(
                count = introSliderItems.size,
                state = pagerState) { page ->

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Image(
                        painter = painterResource(id = introSliderItems[page].image),
                        contentDescription = introSliderItems[page].title + " Images",
                        alignment = Alignment.Center,
                        modifier = Modifier.wrapContentSize(),
                    )


                    Spacer(modifier = Modifier.height(22.dp))

                    Text(modifier = Modifier.padding(all = 10.dp),
                        text = introSliderItems[page].title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onBackground)

                    Spacer(modifier = Modifier.height(15.dp))

                    Text(
                        text = introSliderItems[page].description,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.fillMaxWidth(0.76f))


                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            PagerIndicator(pagerState = pagerState, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp))

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
@Preview
@Composable
fun IntroSliderItemPreview() {
    IntroSliderItem(PagerState(currentPage = 0))
}


