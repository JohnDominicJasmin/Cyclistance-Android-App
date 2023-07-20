package com.example.cyclistance.feature_authentication.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.example.cyclistance.core.domain.model.AlertDialogState

@Composable
fun AlertDialogState.visible(): Boolean {
    return remember(this.title, this.description){
        derivedStateOf {
            this.run { title.isNotEmpty() || description.isNotEmpty() }
        }
    }.value
}