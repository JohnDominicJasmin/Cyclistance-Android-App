package com.example.cyclistance.core.presentation.dialogs.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DropDownMenu(
    iconImageVector: ImageVector = Icons.Default.MoreVert,
    modifier: Modifier = Modifier,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {

        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = iconImageVector,
                contentDescription = "More"
            )
        }

        DropdownMenu(
            modifier = Modifier
                .background(MaterialTheme.colors.secondary)
                .wrapContentSize(),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {

            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = { onClickEdit(); expanded = false }
            )

            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = { onClickDelete(); expanded = false }
            )
        }
    }
}