package com.example.cyclistance.feature_mapping.presentation

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension

val drawerMappingConstraintSet = ConstraintSet {


    val upperSection = createRefFor(id = MappingDrawerConstraintItem.UpperSection.layoutId)
    val bottomSection = createRefFor(id = MappingDrawerConstraintItem.BottomSection.layoutId)



    constrain(upperSection){
        top.linkTo(parent.top)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.matchParent
        height = Dimension.percent(0.25f)
    }



    constrain(bottomSection){
        top.linkTo(upperSection.bottom, margin = 30.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.matchParent
        height = Dimension.percent(0.7f)
    }
}