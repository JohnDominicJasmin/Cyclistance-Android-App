package com.example.cyclistance.navigation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class NavUiState(
    val internetAvailable: Boolean = false,
    val isNavigating: Boolean = false,
    val startingDestination: String = "",
    val drawerPhotoUrl: String? = null,
    val drawerDisplayName: String? = null,
    val conversationUser: MessagingUserItemModel? = null,
    val emergencyContactOnEditMode: Boolean = false,
):Parcelable
