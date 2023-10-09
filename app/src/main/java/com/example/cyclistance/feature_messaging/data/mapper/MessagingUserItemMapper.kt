package com.example.cyclistance.feature_messaging.data.mapper

import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_EMAIL
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_NAME
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_PHOTO
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_UID
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.google.firebase.firestore.DocumentSnapshot

object MessagingUserItemMapper {
    fun DocumentSnapshot.toMessageUser(): MessagingUserItemModel {
        return MessagingUserItemModel(
            userDetails = UserDetails(
                uid = this[KEY_UID].toString(),
                name = this[KEY_NAME].toString(),
                photo = getString(KEY_PHOTO) ?: IMAGE_PLACEHOLDER_URL,
                email = this[KEY_EMAIL].toString(),
            ),
            fcmToken = this[MessagingConstants.KEY_FCM_TOKEN].toString(),
        )
    }

    
}