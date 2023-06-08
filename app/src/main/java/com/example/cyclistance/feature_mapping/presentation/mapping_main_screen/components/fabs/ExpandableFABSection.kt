package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping.presentation.common.BadgeCount
import com.example.cyclistance.theme.CyclistanceTheme
import kotlin.math.roundToInt


@Composable
fun ExpandableFABSection(
    onClickRescueRequest: () -> Unit,
    onClickFamilyTracker: () -> Unit,
    onClickEmergencyCall: () -> Unit,
    isFabExpanded: Boolean,
    onClickFab: () -> Unit,
    badgeCount: Int,
    modifier: Modifier = Modifier
) {
    val animationSpec: AnimationSpec<Float> = spring(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioNoBouncy
    )
    val density = LocalDensity.current.density
    val offsetRescueRequestY = animateFloatAsState(
        targetValue = if (isFabExpanded) -500f * density / 2.75f else 0f,
        animationSpec = animationSpec
    )
    val offsetFamilyTrackerY = animateFloatAsState(
        targetValue = if (isFabExpanded) -350f * density / 2.75f else 0f,
        animationSpec = animationSpec
    )

    val offsetEmergencyCallY = animateFloatAsState(
        targetValue = if (isFabExpanded) -200f * density / 2.75f else 0f,
        animationSpec = animationSpec
    )
    val rotationAngle = animateFloatAsState(
        targetValue = if (isFabExpanded) 45f else 0f,
        animationSpec = animationSpec
    )

    val hasRespondents by remember(badgeCount) {
        derivedStateOf { badgeCount > 0 }
    }


    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        Box(contentAlignment = Alignment.TopEnd) {
            FABItem(
                modifier = Modifier.size(47.dp),
                offset = IntOffset(0, offsetRescueRequestY.value.roundToInt()),
                onClick = {
                    onClickFab()
                    onClickRescueRequest()
                },
                iconSize = 25.dp,
                resId = R.drawable.ic_rescue_request,
                backgroundColor = MaterialTheme.colors.surface,
                iconColor = MaterialTheme.colors.onSurface
            )
            AnimatedVisibility(
                modifier = Modifier.offset {
                    IntOffset(
                        0,
                        offsetRescueRequestY.value.roundToInt())
                },
                visible = isFabExpanded && hasRespondents,
                enter = fadeIn(),
                exit = fadeOut()) {

                BadgeCount(
                    modifier = Modifier.padding(bottom = 20.dp),
                    count = badgeCount.toString()
                )
            }
        }

        FABItem(
            modifier = Modifier.size(47.dp),
            offset = IntOffset(0, offsetFamilyTrackerY.value.roundToInt()),
            onClick = {
                onClickFab()
                onClickFamilyTracker()
            },
            iconSize = 25.dp,
            resId = R.drawable.ic_family_tracker,
            backgroundColor = MaterialTheme.colors.surface,
            iconColor = MaterialTheme.colors.onSurface
        )

        FABItem(
            modifier = Modifier.size(47.dp),
            offset = IntOffset(0, offsetEmergencyCallY.value.roundToInt()),
            onClick = {
                onClickFab()
                onClickEmergencyCall()
            },
            iconSize = 25.dp,
            resId = R.drawable.ic_emergency_call,
            backgroundColor = MaterialTheme.colors.surface,
            iconColor = MaterialTheme.colors.onSurface
        )


        Box(contentAlignment = Alignment.TopEnd) {
            FABItem(
                modifier = Modifier
                    .rotate(rotationAngle.value)
                    .size(53.dp),
                onClick = onClickFab,
                iconSize = 33.dp,
                resId = R.drawable.baseline_add_24,
                backgroundColor = MaterialTheme.colors.primaryVariant,
                iconColor = MaterialTheme.colors.onPrimary
            )

            AnimatedVisibility(
                modifier = Modifier,
                visible = !isFabExpanded && hasRespondents,
                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessHigh)),
                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))) {

                BadgeCount(
                    modifier = Modifier.padding(bottom = 20.dp),
                    count = badgeCount.toString()
                )
            }
        }


    }
}

@Preview
@Composable
fun PreviewExpandableFABSection() {

    var isFabExpanded by remember { mutableStateOf(false) }
    CyclistanceTheme(darkTheme = true) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)) {
            ExpandableFABSection(
                onClickRescueRequest = { },
                onClickFamilyTracker = { },
                onClickEmergencyCall = { },
                badgeCount = 99,
                isFabExpanded = isFabExpanded,
                onClickFab = {

                    isFabExpanded = !isFabExpanded

                })
        }
    }
}