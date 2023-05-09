@file:OptIn(ExperimentalPagerApi::class)

package com.example.cyclistance.feature_readable_displays.presentation.intro_slider

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    val introSliderState by introSliderViewModel.state.collectAsStateWithLifecycle()


    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val navigationDestinationChange = remember(introSliderState.navigationStartingDestination) {
        introSliderState.navigationStartingDestination != Screens.IntroSliderScreen.route
    }

    if (navigationDestinationChange) {
        LaunchedEffect(key1 = true) {
            navController.navigateScreenInclusively(
                destination = introSliderState.navigationStartingDestination,
                popUpToDestination = Screens.IntroSliderScreen.route)
        }
        return
    }

    val onClickSkipButton = remember {
        {
            introSliderViewModel.onEvent(event = IntroSliderEvent.UserCompletedWalkThrough)
            navController.navigateScreenInclusively(
                Screens.SignInScreen.route,
                Screens.IntroSliderScreen.route)
        }
    }
    val onClickNextButton = remember(pagerState.currentPage) {
        {
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
        }
    }


    IntroSliderContent(
        modifier = Modifier

            .padding(paddingValues),
        pagerState = pagerState,
        onClickSkipButton = onClickSkipButton,
        onClickNextButton = onClickNextButton)

}


@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun IntroSliderPreview() {
    CyclistanceTheme(darkTheme = true) {
        IntroSliderContent(
            modifier = Modifier
                .background(MaterialTheme.colors.background),
            pagerState = rememberPagerState(),
            onClickSkipButton = {},
            onClickNextButton = {}
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun IntroSliderContent(
    modifier: Modifier,
    pagerState: PagerState,
    onClickSkipButton: () -> Unit,
    onClickNextButton: () -> Unit) {


    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            ConstraintLayout(
                constraintSet = introSliderConstraints) {

                IntroSliderItem(pagerState = pagerState)

                val isOnLastPage by remember {
                    derivedStateOf { pagerState.currentPage == 2 }
                }

                IntroSliderButtons(
                    text = if (isOnLastPage) "Let's get Started!" else "Next",
                    onClickSkipButton = onClickSkipButton,
                    onClickNextButton = onClickNextButton)
            }
        }
    }
}





