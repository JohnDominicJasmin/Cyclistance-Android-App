package com.myapp.cyclistance.feature_on_boarding.presentation

sealed class IntroSliderEvent {
    object UserCompletedWalkThrough: IntroSliderEvent()
}