package com.myapp.cyclistance.feature_messaging.data.data_source.remote.header

import android.content.Context
import com.myapp.cyclistance.R
import com.myapp.cyclistance.core.utils.constants.MessagingConstants.REMOTE_MSG_AUTHORIZATION
import com.myapp.cyclistance.core.utils.constants.MessagingConstants.REMOTE_MSG_CONTENT_FORMAT
import com.myapp.cyclistance.core.utils.constants.MessagingConstants.REMOTE_MSG_CONTENT_TYPE

object RemoteHeader {

    fun getRemoteMsgHeader(context: Context): HashMap<String, String> {
        return hashMapOf(
            REMOTE_MSG_AUTHORIZATION to "key=${context.getString(R.string.FcmServerKey)}",
            REMOTE_MSG_CONTENT_TYPE to REMOTE_MSG_CONTENT_FORMAT)

    }

}