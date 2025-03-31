package com.myapp.cyclistance.feature_on_boarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.myapp.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalPagerApi::class)
@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewIntroSliderDark() {
    CyclistanceTheme(darkTheme = true) {
        IntroSliderContent(
            modifier = Modifier
                .background(MaterialTheme.colors.background),
            pagerState = rememberPagerState(),
            onClickSkipButton = {},
            onClickNextButton = {},
            isOnLastPage = true
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewIntroSliderLight() {
    CyclistanceTheme(darkTheme = false) {
        IntroSliderContent(
            modifier = Modifier
                .background(MaterialTheme.colors.background),
            pagerState = rememberPagerState(1),
            onClickSkipButton = {},
            onClickNextButton = {},
            isOnLastPage = true
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroSliderContent(
    modifier: Modifier,
    pagerState: PagerState,
    isOnLastPage: Boolean,
    onClickSkipButton: () -> Unit,
    onClickNextButton: () -> Unit) {


    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {


            IntroSliderItem(
                pagerState = pagerState,
                modifier = Modifier
                    .weight(0.7f)
            )



            IntroSliderButtonSection(
                modifier = Modifier
                    .weight(0.3f),
                text = if (isOnLastPage) "Let's get Started!" else "Next",
                onClickSkipButton = onClickSkipButton,
                onClickNextButton = onClickNextButton)
        }
    }
}





