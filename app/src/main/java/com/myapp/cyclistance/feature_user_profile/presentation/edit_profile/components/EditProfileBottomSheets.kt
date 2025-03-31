package com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun SelectImageBottomSheet(
    isLoading: Boolean,
    onClickGalleryButton: () -> Unit,
    onClickCameraButton: () -> Unit,
    bottomSheetScaffoldState: ModalBottomSheetState,
    content: @Composable () -> Unit) {


    ModalBottomSheetLayout(
        sheetState = bottomSheetScaffoldState,
        sheetContent = {
            Card(
                modifier = Modifier.fillMaxWidth()) {


                Column(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)) {

                    BottomSheetButtonItem(
                        iconId = R.drawable.ic_gallery,
                        buttonText = "Open Gallery",
                        onClick = onClickGalleryButton,
                        enabled = !isLoading)

                    BottomSheetButtonItem(
                        iconId = R.drawable.ic_camera,
                        buttonText = "Take Photo",
                        onClick = onClickCameraButton,
                        enabled = !isLoading)
                }

            }
        },
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetElevation = 12.dp,
        sheetBackgroundColor = MaterialTheme.colors.surface,
        sheetContentColor = MaterialTheme.colors.onSurface,
        scrimColor = ModalBottomSheetDefaults.scrimColor.copy(alpha = 0.10f),
        content = content
    )


}


@Composable
fun BottomSheetButtonItem(
    @DrawableRes iconId: Int,
    buttonText: String,
    onClick: () -> Unit,
    enabled: Boolean) {

    TextButton(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp)) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(0.3f)
                .padding(start = 8.5.dp)) {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "$buttonText Icon",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.padding(start = 3.dp, end = 12.dp))

            Text(
                text = buttonText,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.button,

                )
        }
    }

}

@ExperimentalMaterialApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BottomSheetPreview() {
    val bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val scope = rememberCoroutineScope()

    SelectImageBottomSheet(
        onClickGalleryButton = {},
        onClickCameraButton = {},
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        isLoading = false) {
        Button(onClick = {
            scope.launch {

                if (bottomSheetScaffoldState.isVisible) {
                    bottomSheetScaffoldState.hide()
                } else {
                    bottomSheetScaffoldState.show()
                }
            }
        }) {
            Text(text = "Expand/Collapse Bottom Sheet")
        }
    }
}