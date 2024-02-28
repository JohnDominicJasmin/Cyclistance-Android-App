package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.state.MappingState
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun RequestHelpButton(
    modifier: Modifier = Modifier,
    onClickRequestHelpButton: () -> Unit = {},
    state: MappingState = MappingState(),
    visible: Boolean = true) {


    AnimatedVisibility(
        modifier = modifier, visible = visible, enter = fadeIn(
            initialAlpha = 0.4f
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 100)
        )) {

        Button(
            enabled = !state.isLoading,
            onClick = onClickRequestHelpButton,
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
            onClickRequestHelpButton = { },
            modifier = Modifier,
            state = MappingState())
    }
}