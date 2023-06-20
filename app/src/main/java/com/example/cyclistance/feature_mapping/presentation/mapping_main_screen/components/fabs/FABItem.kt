package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.utils.composable_utils.drawColoredShadow


@Composable
fun FABItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    iconSize: Dp = 25.dp,
    @DrawableRes resId: Int,
    backgroundColor: Color,
    iconColor: Color
) {

    FloatingActionButton(
        modifier = modifier
            .size(68.dp)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    drawColoredShadow(
                        alpha = 0.15f,
                        shadowRadius = 5.dp,
                        color = MaterialTheme.colors.surface,
                        borderRadius = 34.dp
                    )
                }
            },
        onClick = onClick,
        shape = CircleShape,
        backgroundColor = backgroundColor,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 2.dp,
            pressedElevation = 12.dp,
            hoveredElevation = 8.dp,
            focusedElevation = 8.dp,

            )) {

        Icon(
            painter = painterResource(resId),
            contentDescription = "FAB",
            modifier = Modifier.size(iconSize),
            tint = iconColor)

    }

}