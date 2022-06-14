@file:OptIn(ExperimentalPagerApi::class)

package com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.IntroSliderEvent
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.IntroSliderViewModel
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun IntroSliderScreen(
    introSliderViewModel: IntroSliderViewModel = hiltViewModel(),
    onBackPressed: () -> Unit,
    navigateTo: (destination: String, popUpToDestination: String?) -> Unit,) {

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    BackHandler(enabled = true, onBack = onBackPressed)


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {
        ConstraintLayout(
            constraintSet = introSliderConstraints,
            modifier = Modifier
                .background(MaterialTheme.colors.background)) {

            IntroSliderItem(pagerState = pagerState)

            val isOnLastPage = pagerState.currentPage == 2

            IntroSliderButtons(
                text = if (isOnLastPage) "Let's get Started!" else "Next",

                onClickSkipButton = {
                    introSliderViewModel.onEvent(event = IntroSliderEvent.UserCompletedWalkThrough)
                    navigateTo(Screens.SignInScreen.route, Screens.IntroSliderScreen.route)
                },
                onClickNextButton = {
                    if (pagerState.currentPage != 2) {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage + 1
                            )
                        }
                    } else {
                        introSliderViewModel.onEvent(event = IntroSliderEvent.UserCompletedWalkThrough)
                        navigateTo(Screens.SignInScreen.route, Screens.IntroSliderScreen.route)
                    }
                })
        }
    }
}

@Preview
@Composable
fun IntroSliderScreenPreview() {
    CyclistanceTheme(false) {
        IntroSliderScreen(onBackPressed = {  }, navigateTo = {_,_->})
    }

}






