package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.dialog.report_incident

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.CyclistanceTheme

@Composable
fun ViewImageButton(modifier: Modifier = Modifier, viewImage: () -> Unit) {

    Button(
        modifier = modifier,
        onClick = viewImage,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        shape = RoundedCornerShape(8.dp)) {

        Icon(
            painter = painterResource(id = R.drawable.ic_view_image),
            contentDescription = "View Image",
            modifier = Modifier.padding(end = 4.dp)
        )

        Text(
            text = "View Image",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.button,

        )
    }
}

@Preview
@Composable
fun PreviewViewImageButton() {
    CyclistanceTheme(darkTheme = true) {
        ViewImageButton(
            viewImage = {}
        )
    }
}