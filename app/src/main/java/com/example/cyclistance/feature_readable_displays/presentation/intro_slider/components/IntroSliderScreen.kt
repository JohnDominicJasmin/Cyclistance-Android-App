package com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.IntroSliderViewModel
import com.example.cyclistance.navigation.Screens
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroSliderScreen(
    onBackPressed: () -> Unit,
    navController: NavController,
    introSliderViewModel: IntroSliderViewModel = hiltViewModel()) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    BackHandler(enabled = true, onBack = onBackPressed)


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

    navController.navigate(Screens.SignInScreen.route){
        popUpTo(Screens.IntroSliderScreen.route){
            inclusive = true

        }
        launchSingleTop = true

    }
}

