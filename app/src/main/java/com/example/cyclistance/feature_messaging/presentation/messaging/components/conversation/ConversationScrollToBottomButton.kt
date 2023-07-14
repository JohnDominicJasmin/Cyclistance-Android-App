package com.example.cyclistance.feature_messaging.presentation.messaging.components.conversation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScrollToBottomButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onClick: () -> Unit = {}) {

    AnimatedVisibility(
        visible = isVisible,
        modifier = modifier,
        enter = scaleIn(),
        exit = scaleOut()) {

        Surface(
            modifier = Modifier.size(40.dp),
            onClick = onClick,
            shape = CircleShape,
            contentColor = MaterialTheme.colors.primary,
            color = MaterialTheme.colors.surface,
            elevation = 1.5.dp) {

            Icon(
                imageVector = Icons.Default.ArrowDownward,
                contentDescription = "Scroll to bottom",
                modifier = Modifier.padding(all = 10.dp))
        }

    }
}

@Preview
@Composable
fun PreviewScrollToBottomButtonDark() {
    CyclistanceTheme(darkTheme = true) {
        ScrollToBottomButton(isVisible = true, onClick = {})
    }
}


@Preview
@Composable
fun PreviewScrollToBottomButtonLight() {
    CyclistanceTheme(darkTheme = false) {
        ScrollToBottomButton(isVisible = true, onClick = {})
    }
}

