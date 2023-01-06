package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RequestHelpButton(
    modifier: Modifier = Modifier,
    onClickSearchButton: () -> Unit = {},
    state: MappingState = MappingState()) {

    val visibility =  state.requestHelpButtonVisible && state.isNavigating.not()

    AnimatedVisibility(
        modifier = modifier, visible = visibility, enter = fadeIn(
            initialAlpha = 0.4f
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 100)
        )) {
        Button(
            enabled = !state.isLoading,
            onClick = onClickSearchButton,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier) {


            Text(
                text = "Request Help",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.button,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    top = 4.dp,
                    bottom = 4.dp,
                    start = 20.dp,
                    end = 12.dp)
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Request Help",
                tint = MaterialTheme.colors.onPrimary,
            )
        }
    }
}

@Preview
@Composable
fun RequestHelpButtonPreview() {

    CyclistanceTheme(true) {
        RequestHelpButton(
            onClickSearchButton = { },
            modifier = Modifier,
            state = MappingState())
    }
}