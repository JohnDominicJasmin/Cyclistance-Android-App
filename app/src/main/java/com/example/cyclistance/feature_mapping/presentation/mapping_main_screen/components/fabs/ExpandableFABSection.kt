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
    modifier: Modifier = Modifier,
    onClickRescueRequest: () -> Unit = {},
    onClickFamilyTracker: () -> Unit = {},
    onClickEmergencyCall: () -> Unit = {},
    onClickBikeTracker: () -> Unit = {},
    isFabExpanded: Boolean,
    onClickFab: () -> Unit = {},
    badgeCount: Int,
) {
    val animationSpec: AnimationSpec<Float> = spring(
        stiffness = Spring.StiffnessLow,
        dampingRatio = Spring.DampingRatioNoBouncy
    )

    val density = LocalDensity.current.density
    val offsetRescueRequestY = animateFloatAsState(
        targetValue = -365f * density / 2.75f,
        animationSpec = animationSpec, label = ""
    )

    val offsetBikeTrackerY = animateFloatAsState(
        targetValue = -290f * density / 2.7f,
        animationSpec = animationSpec, label = ""
    )

    val offsetFamilyTrackerY = animateFloatAsState(
        targetValue = -155f * density / 2.75f,
        animationSpec = animationSpec, label = ""
    )

    val offsetEmergencyCallY = animateFloatAsState(
        targetValue = 0f * density / 2.75f,
        animationSpec = animationSpec, label = ""
    )
    val rotationAngle = animateFloatAsState(
        targetValue = if (isFabExpanded) 45f else 0f,
        animationSpec = animationSpec, label = ""
    )

    val hasRespondents by remember(badgeCount) {
        derivedStateOf { badgeCount > 0 }
    }


    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        AnimatedVisibility(
            visible = isFabExpanded,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.offset {
                IntOffset(0, offsetRescueRequestY.value.roundToInt())
            }) {


            Box(contentAlignment = Alignment.TopEnd) {

                FABItem(
                    modifier = Modifier.size(47.dp),
                    onClick = {
                        onClickFab()
                        onClickRescueRequest()
                    },
                    resId = R.drawable.ic_rescue_request,
                    backgroundColor = MaterialTheme.colors.surface,
                    iconColor = MaterialTheme.colors.onSurface
                )
                AnimatedVisibility(
                    visible = isFabExpanded && hasRespondents,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    BadgeCount(
                        modifier = Modifier.padding(bottom = 20.dp),
                        count = badgeCount.toString()
                    )
                }
            }
        }


        AnimatedVisibility(
            visible = isFabExpanded,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.offset { IntOffset(-155, offsetBikeTrackerY.value.roundToInt()) }
        ) {
            FABItem(
                modifier = Modifier.size(47.dp),
                onClick = {
                    onClickFab()
                    onClickBikeTracker()
                },
                resId = R.drawable.ic_sinotrack,
                backgroundColor = MaterialTheme.colors.surface,
                iconColor = Color.Unspecified
            )
        }


        AnimatedVisibility(
            visible = isFabExpanded,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.offset { IntOffset(-250, offsetFamilyTrackerY.value.roundToInt()) }
        ) {

            FABItem(
                modifier = Modifier.size(47.dp),
                onClick = {
                    onClickFab()
                    onClickFamilyTracker()
                },
                resId = R.drawable.ic_family_tracker,
                backgroundColor = MaterialTheme.colors.surface,
                iconColor = MaterialTheme.colors.onSurface
            )

        }

        AnimatedVisibility(visible = isFabExpanded,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.offset {
                IntOffset(
                    -310,
                    offsetEmergencyCallY.value.roundToInt()
                )
            }) {

            FABItem(
                modifier = Modifier.size(47.dp),
                onClick = {
                    onClickFab()
                    onClickEmergencyCall()
                },
                resId = R.drawable.ic_emergency_call,
                backgroundColor = MaterialTheme.colors.surface,
                iconColor = MaterialTheme.colors.onSurface
            )

        }

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
                exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh))
            ) {

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
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            ExpandableFABSection(
                modifier = Modifier.padding(end = 12.dp, bottom = 12.dp),
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