package com.example.cyclistance.feature_readable_displays.presentation.components


sealed class IntroSliderConstraintsItem(val layoutId: String){
    object HorizontalPager: IntroSliderConstraintsItem(layoutId = "horizontal_pager")
    object ButtonSection: IntroSliderConstraintsItem(layoutId = "buttons")
    object PagerIndicator: IntroSliderConstraintsItem(layoutId = "page_indicator")
}
