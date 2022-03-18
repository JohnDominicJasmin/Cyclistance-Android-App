package com.example.cyclistance.feature_readable_displays.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.IntroSliderViewModel
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components.IntroSliderButtons
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components.IntroSliderItem
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components.introSliderConstraints
import com.example.cyclistance.navigation.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroSliderScreen(navController: NavController) {
    val pagerState = rememberPagerState()
    val introSliderViewModel:IntroSliderViewModel = hiltViewModel()
    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)) {
        ConstraintLayout(
            constraintSet = introSliderConstraints,
            modifier = Modifier
                .background(BackgroundColor)) {

            IntroSliderItem(pagerState = pagerState)

            IntroSliderButtons(
                text = if (pagerState.currentPage == 2) "Let's get Started!" else "Next",

                onClickSkipButton = {
                    showSignInScreen(navController, introSliderViewModel)
                },
                onClickNextButton = {
                    if (pagerState.currentPage != 2) {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage + 1
                            )
                        }
                    } else {
                        showSignInScreen(navController, introSliderViewModel)
                    }


                })


        }
    }

}


private fun showSignInScreen(navController: NavController, introSliderViewModel: IntroSliderViewModel){
    introSliderViewModel.userCompletedWalkThrough()
    navController.popBackStack()
    navController.navigate(Screens.SignInScreen.route)
}

