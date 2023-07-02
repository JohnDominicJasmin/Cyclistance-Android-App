@file:OptIn(ExperimentalPagerApi::class)

package com.example.cyclistance.feature_on_boarding.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_on_boarding.presentation.components.IntroSliderContent
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreenInclusively
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroSliderScreen(
    introSliderViewModel: IntroSliderViewModel = hiltViewModel(),
    paddingValues: PaddingValues,
    navController: NavController) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val isOnLastPage by remember {
        derivedStateOf { pagerState.currentPage == 2 }
    }

    val onClickSkipButton = remember {{
            introSliderViewModel.onEvent(event = IntroSliderEvent.UserCompletedWalkThrough)
            navController.navigateScreenInclusively(
                Screens.Authentication.ROUTE,
                Screens.OnBoarding.ROUTE)
        }
    }


    val onClickNextButton = remember(pagerState.currentPage) {
        {
            if (isOnLastPage) {
                introSliderViewModel.onEvent(event = IntroSliderEvent.UserCompletedWalkThrough)
                navController.navigateScreenInclusively(
                    Screens.Authentication.ROUTE,
                    Screens.OnBoarding.ROUTE)
            } else {
                scope.launch {
                    pagerState.animateScrollToPage(
                        page = pagerState.currentPage + 1
                    )
                }
            }
            Unit
        }
    }


    IntroSliderContent(
        modifier = Modifier
            .padding(paddingValues),
        pagerState = pagerState,
        onClickSkipButton = onClickSkipButton,
        onClickNextButton = onClickNextButton,
        isOnLastPage = isOnLastPage)

}


