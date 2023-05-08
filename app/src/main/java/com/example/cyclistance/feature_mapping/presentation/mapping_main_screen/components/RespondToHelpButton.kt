package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RespondToHelpButton(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onClickRespondButton: () -> Unit = {},
    state: MappingState = MappingState()) {


    AnimatedVisibility(
        modifier = modifier, visible = visible, enter = fadeIn(
            initialAlpha = 0.4f
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 150)
        )) {
        Button(
            enabled = !state.isLoading,
            onClick = onClickRespondButton,
            modifier = Modifier,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary)) {

            Icon(
                painter = painterResource(id = R.drawable.ic_help_handshake),
                contentDescription = "Respond to Help",
                tint = MaterialTheme.colors.onPrimary)

            Text(
                text = "Respond to Help", color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.button, fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp))
        }
    }

}

@Preview(name = "RespondToHelpButton")
@Composable
private fun PreviewRespondToHelpButton() {
    CyclistanceTheme(true) {
        RespondToHelpButton()
    }
}