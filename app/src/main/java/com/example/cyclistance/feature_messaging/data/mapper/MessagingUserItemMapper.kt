package com.example.cyclistance.feature_messaging.data.mapper

import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.google.firebase.firestore.DocumentSnapshot

object MessagingUserItemMapper {
    fun DocumentSnapshot.toMessageUser(): MessagingUserItemModel {
        return MessagingUserItemModel(
            userDetails = UserDetails(
                uid = this[MessagingConstants.KEY_UID].toString(),
                name = this[MessagingConstants.KEY_NAME].toString(),
                photo = this[MessagingConstants.KEY_PHOTO].toString(),
                email = this[MessagingConstants.KEY_EMAIL].toString(),
            ),
            fcmToken = this[MessagingConstants.KEY_FCM_TOKEN].toString()

        )
    }

    
}