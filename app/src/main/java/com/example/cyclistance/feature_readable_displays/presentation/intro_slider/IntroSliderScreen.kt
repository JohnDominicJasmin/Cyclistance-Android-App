@file:OptIn(ExperimentalPagerApi::class)

package com.example.cyclistance.feature_readable_displays.presentation.intro_slider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components.IntroSliderButtons
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components.IntroSliderItem
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components.introSliderConstraints
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreenInclusively
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroSliderScreen(
    introSliderViewModel: IntroSliderViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val introSliderState by introSliderViewModel.state.collectAsState()


    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val navigationDestinationChange = remember(introSliderState.navigationStartingDestination,){
            introSliderState.navigationStartingDestination != Screens.IntroSliderScreen.route
    }

    if(navigationDestinationChange){
        LaunchedEffect(key1 = true){
            navController.navigateScreenInclusively(introSliderState.navigationStartingDestination, Screens.IntroSliderScreen.route)
        }
        return
    }

    val onClickSkipButton = remember{
        {
            introSliderViewModel.onEvent(event = IntroSliderEvent.UserCompletedWalkThrough)
            navController.navigateScreenInclusively(
                Screens.SignInScreen.route,
                Screens.IntroSliderScreen.route)
        }
    }
    val onClickNextButton = remember(pagerState.currentPage){{
        if (pagerState.currentPage != 2) {
            scope.launch {
                pagerState.animateScrollToPage(
                    page = pagerState.currentPage + 1
                )
            }
        } else {
            introSliderViewModel.onEvent(event = IntroSliderEvent.UserCompletedWalkThrough)
            navController.navigateScreenInclusively(
                Screens.SignInScreen.route,
                Screens.IntroSliderScreen.route)
        }
        Unit
    }}


    IntroSlider(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
        pagerState = pagerState,
        onClickSkipButton = onClickSkipButton,
        onClickNextButton = onClickNextButton)

}


@Preview
@Composable
fun IntroSliderPreview() {
    CyclistanceTheme(darkTheme = true) {
        IntroSlider(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            pagerState = rememberPagerState(),
            onClickSkipButton = {},
            onClickNextButton = {}
        )
    }
}
@Composable
private fun IntroSlider(
    modifier: Modifier,
    pagerState: PagerState,
    onClickSkipButton: () -> Unit,
    onClickNextButton: () -> Unit) {


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colors.background)) {
        ConstraintLayout(
            constraintSet = introSliderConstraints,
            modifier = Modifier
                .background(MaterialTheme.colors.background)) {

            IntroSliderItem(pagerState = pagerState)

            val isOnLastPage = remember(pagerState.currentPage){
                pagerState.currentPage == 2
            }

            IntroSliderButtons(
                text = if (isOnLastPage) "Let's get Started!" else "Next",
                onClickSkipButton = onClickSkipButton,
                onClickNextButton = onClickNextButton)
        }
    }
}





