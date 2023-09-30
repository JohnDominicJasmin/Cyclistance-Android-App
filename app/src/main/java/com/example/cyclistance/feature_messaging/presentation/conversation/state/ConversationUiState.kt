package com.example.cyclistance.feature_messaging.presentation.conversation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class ConversationUiState(
    val chatItemSelectedIndex: Int = -1,
    val messageAreaExpanded: Boolean = false,
    val notificationPermissionVisible: Boolean = false,
    val resendDialogVisible: Boolean = false
) : Parcelable