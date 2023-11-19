@file:OptIn(ExperimentalPagerApi::class)

package com.myapp.cyclistance.feature_on_boarding.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.myapp.cyclistance.feature_on_boarding.presentation.components.IntroSliderContent
import com.myapp.cyclistance.navigation.Screens
import com.myapp.cyclistance.navigation.nav_graph.navigateScreenInclusively
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
                Screens.AuthenticationNavigation.ROUTE,
                Screens.OnBoardingNavigation.ROUTE)
        }
    }


    val onClickNextButton = remember(pagerState.currentPage) {
        {
            if (isOnLastPage) {
                introSliderViewModel.onEvent(event = IntroSliderEvent.UserCompletedWalkThrough)
                navController.navigateScreenInclusively(
                    Screens.AuthenticationNavigation.ROUTE,
                    Screens.OnBoardingNavigation.ROUTE)
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


