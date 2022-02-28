package com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

val introSliderConstraints = ConstraintSet {
    val horizontalPager = createRefFor(id = IntroSliderConstraintsItem.HorizontalPager.layoutId)
    val buttonSection = createRefFor(id = IntroSliderConstraintsItem.ButtonSection.layoutId)
    val pageIndicator = createRefFor(id = IntroSliderConstraintsItem.PagerIndicator.layoutId)


    constrain(horizontalPager){
        top.linkTo(parent.top, margin = 30.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        height = Dimension.wrapContent
        width = Dimension.matchParent
    }



    constrain(pageIndicator){
        top.linkTo(horizontalPager.bottom)
        height = Dimension.wrapContent
        width = Dimension.wrapContent
    }

    constrain(buttonSection){
        top.linkTo(pageIndicator.bottom, margin = 5.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.matchParent
        height = Dimension.wrapContent
    }

}