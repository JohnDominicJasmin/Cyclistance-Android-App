package com.example.cyclistance.feature_messaging.data.repository

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_AVAILABILITY
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_COLLECTION_CHATS
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_CONVERSATIONS_COLLECTION
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_FCM_TOKEN
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_IS_SEEN
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_LAST_MESSAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_MESSAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_RECEIVER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_SENDER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_TIMESTAMP
import com.example.cyclistance.core.utils.constants.MessagingConstants.RECEIVER_MESSAGE_OBJ
import com.example.cyclistance.core.utils.constants.MessagingConstants.REMOTE_MSG_DATA
import com.example.cyclistance.core.utils.constants.MessagingConstants.REMOTE_MSG_REGISTRATION_IDS
import com.example.cyclistance.core.utils.constants.MessagingConstants.SAVED_TOKEN
import com.example.cyclistance.core.utils.constants.MessagingConstants.SENDER_MESSAGE_OBJ
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_NAME
import com.example.cyclistance.core.utils.constants.UtilConstants.USER_COLLECTION
import com.example.cyclistance.core.utils.contexts.dataStore
import com.example.cyclistance.core.utils.data_store_ext.editData
import com.example.cyclistance.core.utils.data_store_ext.getData
import com.example.cyclistance.feature_messaging.data.MessagingApi
import com.example.cyclistance.feature_messaging.data.data_source.remote.header.RemoteHeader
import com.example.cyclistance.feature_messaging.data.mapper.MessagingChatItemMapper.toConversionChatItem
import com.example.cyclistance.feature_messaging.data.mapper.MessagingConversationItemMapper.toConversationItem
import com.example.cyclistance.feature_messaging.data.mapper.MessagingUserItemMapper.toMessageUser
import com.example.cyclistance.feature_messaging.domain.exceptions.MessagingExceptions
import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.SendNotificationModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel.Companion.toJsonString
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.Date
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MessagingRepositoryImpl(
    private val fireStore: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging,
    private val auth: FirebaseAuth,
    private val appContext: Context,
    private val api: MessagingApi,

    ) : MessagingRepository {
    private var messageListener: ListenerRegistration? = null
    private var chatListener: ListenerRegistration? = null
    private var messageUserListener: ListenerRegistration? = null
    private val scope: CoroutineContext = Dispatchers.IO
    private var dataStore = appContext.dataStore

    override suspend fun markAsSeen(messageId: String, conversionId: String) {
        try {

            coroutineScope {

                val conversationUpdateTask = async {
                    fireStore
                        .collection(KEY_CONVERSATIONS_COLLECTION)
                        .document(messageId)
                        .update(KEY_IS_SEEN, true)
                }

                val chatUpdateTask = async {
                    fireStore
                        .collection(KEY_COLLECTION_CHATS)
                        .document(conversionId)
                        .update(KEY_IS_SEEN, true)
                }

                awaitAll(conversationUpdateTask, chatUpdateTask)
            }
        } catch (e: Exception) {
            throw MessagingExceptions.MarkAsSeenFailure(
                message = e.message ?: "Mark as seen failure"
            )
        }
    }

    override suspend fun reEnableNetworkSync() {
        suspendCoroutine { continuation ->
            fireStore.enableNetwork().addOnCompleteListener {
                continuation.resume(Unit)
            }.addOnFailureListener {
                continuation.resumeWithException(MessagingExceptions.ReSyncFailure(message = it.message!!))
            }
        }
    }

    override suspend fun getMessagingUser(uid: String): MessagingUserItemModel {

        return suspendCoroutine {
            fireStore.collection(USER_COLLECTION)
                .document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    it.resume(documentSnapshot.toMessageUser())
                }.addOnFailureListener { exception ->
                    it.resumeWithException(MessagingExceptions.GetMessageUsersFailure(message = exception.message!!))
                }
        }
    }


    override fun addChatListener(
        onAddedChat: (ChatItemModel) -> Unit,
        onModifiedChat: (ChatItemModel) -> Unit
    ) {
        val userId = getUid()

        chatListener = fireStore.collection(KEY_COLLECTION_CHATS)
            .where(
                Filter.or(
                    Filter.equalTo(KEY_RECEIVER_ID, userId),
                    Filter.equalTo(KEY_SENDER_ID, userId)
                )
            )
            .orderBy(KEY_TIMESTAMP, Query.Direction.ASCENDING)
            .addSnapshotListener(
                MetadataChanges.INCLUDE,
                chatListener(
                    onAddedChat = onAddedChat,
                    onModifiedChat = onModifiedChat
                )
            )

    }

    override suspend fun getConversionId(receiverId: String): String {
        val userId = getUid()
        return suspendCancellableCoroutine { continuation ->

            fireStore.collection(KEY_COLLECTION_CHATS)
                .where(
                    Filter.and(
                        Filter.or(
                            Filter.equalTo(KEY_SENDER_ID, userId),
                            Filter.equalTo(KEY_SENDER_ID, receiverId),
                        ),
                        Filter.or(
                            Filter.equalTo(KEY_RECEIVER_ID, userId),
                            Filter.equalTo(KEY_RECEIVER_ID, receiverId)
                        )
                    )
                )
                .get()
                .addOnCompleteListener { task ->

                    val documents = task.result?.documents ?: emptyList()
                    if (!task.isSuccessful) {
                        return@addOnCompleteListener
                    }

                    if (documents.isEmpty()) {
                        return@addOnCompleteListener
                    }

                    val documentSnapshot = documents.first()
                    continuation.resume(documentSnapshot.id)
                }
        }
    }

    override fun addConversion(
        conversion: HashMap<String, Any>,
        onNewConversionId: (String) -> Unit
    ) {

        fireStore.collection(KEY_COLLECTION_CHATS)
            .add(conversion)
            .addOnSuccessListener { documentReference ->
                onNewConversionId(documentReference.id)
            }
    }


    override fun updateConversion(message: String, conversionId: String) {
        fireStore.collection(KEY_COLLECTION_CHATS)
            .document(conversionId)
            .update(KEY_LAST_MESSAGE, message, KEY_TIMESTAMP, Date(), KEY_IS_SEEN, false)
            .addOnSuccessListener {
                Timber.v("Conversion updated successfully")
            }.addOnFailureListener {
                Timber.e("Conversion update failed ${it.message}")
            }
    }

    override fun getUserUid(): String {
        return getUid()
    }

    override fun addMessageListener(
        receiverId: String,
        onNewMessage: (ConversationsModel) -> Unit
    ) {

        val userUid = getUid()

        messageListener = fireStore.collection(KEY_CONVERSATIONS_COLLECTION)
            .where(
                Filter.and(
                    Filter.or(
                        Filter.equalTo(KEY_SENDER_ID, userUid),
                        Filter.equalTo(KEY_SENDER_ID, receiverId),
                    ),
                    Filter.or(
                        Filter.equalTo(KEY_RECEIVER_ID, userUid),
                        Filter.equalTo(KEY_RECEIVER_ID, receiverId)
                    )
                )
            )
            .orderBy(KEY_TIMESTAMP, Query.Direction.ASCENDING)
            .addSnapshotListener(MetadataChanges.INCLUDE, messageListener(onNewMessage))

    }

    override suspend fun refreshToken() {
        withContext(scope) {

            val token = getMessagingToken()
            val savedToken = dataStore.getData(key = SAVED_TOKEN, defaultValue = "").firstOrNull()

            if (token == savedToken) {
                return@withContext
            }

            updateMessagingToken(token = token)
        }
    }

    override fun addUserListener(onNewMessageUser: (MessagingUserModel) -> Unit) {

        messageUserListener = fireStore.collection(USER_COLLECTION)
            .addSnapshotListener(MetadataChanges.INCLUDE) { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                if (value == null) {
                    throw MessagingExceptions.GetMessageUsersFailure(message = "Cannot get message users, value is null")
                }
                if (error != null) {
                    throw MessagingExceptions.GetMessageUsersFailure(
                        message = error.message ?: "Unknown error occurred"
                    )
                }

                val users = value.documentChanges.map {
                    it.document.toMessageUser()
                }

                if (users.isNotEmpty()) {
                    onNewMessageUser(
                        MessagingUserModel(
                            users = users
                        )
                    )
                }

            }


    }

    override fun updateUserAvailability(isUserAvailable: Boolean) {
        val uid = getUid()
        fireStore.collection(USER_COLLECTION)
            .document(uid)
            .update(KEY_AVAILABILITY, isUserAvailable)
            .addOnSuccessListener {
                Timber.v("User availability updated successfully")
            }.addOnFailureListener {
                Timber.v("User availability update failed")
            }
    }

    override fun removeMessageListener() {
        messageListener?.remove()
    }

    override fun removeUserListener() {
        messageUserListener?.remove()
    }

    override fun removeChatListener() {
        chatListener?.remove()
    }

    override suspend fun sendMessage(sendMessageModel: SendMessageModel) {

        val uid = getUid()
        val message = mapOf(
            KEY_SENDER_ID to uid,
            KEY_RECEIVER_ID to sendMessageModel.receiverId,
            KEY_MESSAGE to sendMessageModel.message.trimEnd(),
            KEY_TIMESTAMP to Date(),
            KEY_IS_SEEN to false

        )

        suspendCancellableCoroutine { continuation ->
            fireStore
                .collection(KEY_CONVERSATIONS_COLLECTION)
                .add(message)
                .addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(
                        MessagingExceptions.SendMessagingFailure(
                            message = it.message!!
                        )
                    )
                }.addOnCanceledListener {
                    Timber.e("Message sending cancelled by user")
                }
        }

    }

    override suspend fun deleteToken() {
        checkInternetConnection()
        dataStore.editData(key = SAVED_TOKEN, value = "")
        suspendCancellableCoroutine { continuation ->
            fireStore.collection(
                USER_COLLECTION
            ).document(getUid()).update(KEY_FCM_TOKEN, FieldValue.delete()).addOnSuccessListener {
                continuation.resume(Unit)
            }.addOnFailureListener {
                continuation.resumeWithException(MessagingExceptions.TokenException(message = it.message!!))
            }
        }
    }

    override suspend fun sendNotification(model: SendNotificationModel) {

        val tokens = JSONArray().put(model.userReceiverMessage.fcmToken)
        val data = JSONObject().apply {
            put(KEY_NAME, model.senderName)
            put(KEY_MESSAGE, model.message)
            put(RECEIVER_MESSAGE_OBJ, model.userReceiverMessage.toJsonString())
            put(SENDER_MESSAGE_OBJ, model.userSenderMessage.toJsonString())
        }

        val body = JSONObject().apply {
            put(REMOTE_MSG_DATA, data)
            put(REMOTE_MSG_REGISTRATION_IDS, tokens)
        }


        api.sendMessage(
            headers = RemoteHeader.getRemoteMsgHeader(appContext),
            message = body.toString()
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Timber.v("Notification sent successfully ${response.isSuccessful} | ${response.raw()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Timber.v("Notification sending failed ${t.message}")
            }

        })
    }


    private suspend fun getMessagingToken(): String {
        return suspendCancellableCoroutine { continuation ->
            firebaseMessaging.token.addOnSuccessListener { token: String ->
                if (continuation.isActive) {
                    continuation.resume(token)
                }
            }.addOnFailureListener {
                continuation.resumeWithException(
                    MessagingExceptions.TokenException(
                        it.message!!
                    )
                )
            }
        }
    }

    private fun getUid(): String {
        return auth.uid ?: throw MessagingExceptions.TokenException(message = "User not logged in")
    }


    private suspend fun updateMessagingToken(token: String) {
        val uid = getUid()

        try {
            fireStore.collection(USER_COLLECTION).document(uid).update(
                KEY_FCM_TOKEN, token
            ).await()

            withContext(Dispatchers.IO) {
                dataStore.editData(key = SAVED_TOKEN, value = token)
            }
        } catch (exception: Exception) {
            throw MessagingExceptions.TokenException(
                message = exception.message ?: "Token update failed"
            )
        }
    }

    private fun checkInternetConnection() {
        if (!appContext.hasInternetConnection()) {
            throw MessagingExceptions.NetworkException(message = appContext.getString(R.string.no_internet_message))
        }
    }

    private fun messageListener(onNewMessage: (ConversationsModel) -> Unit): (QuerySnapshot?, FirebaseFirestoreException?) -> Unit {
        return { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
            if (value == null) {
                throw MessagingExceptions.ListenMessagingFailure(message = "Cannot listen to messages, value is null")
            }

            if (error != null) {
                throw MessagingExceptions.ListenMessagingFailure(
                    message = error.message ?: "Unknown error occurred"
                )
            }

            val messages: List<ConversationItemModel> =
                value.documents
                    .map {
                        val conversationItem = it.toConversationItem()
                        conversationItem.copy(isSent = !it.isDeviceOffline())
                    }

            onNewMessage(
                ConversationsModel(
                    messages = messages
                )
            )
        }
    }

    private fun DocumentSnapshot.isDeviceOffline(): Boolean {
        return with(this.metadata) { isFromCache.and(hasPendingWrites()) }
    }

    private inline fun chatListener(
        crossinline onAddedChat: (ChatItemModel) -> Unit,
        crossinline onModifiedChat: (ChatItemModel) -> Unit
    ): (QuerySnapshot?, FirebaseFirestoreException?) -> Unit {

        return { value: QuerySnapshot?, error: FirebaseFirestoreException? ->

            val uid = getUid()

            if (error != null) {
                throw MessagingExceptions.GetChatsFailure(
                    message = error.message ?: "Unknown error occurred"
                )
            }

            if (value == null) {
                throw MessagingExceptions.GetChatsFailure(message = "Cannot get chats, value is null")
            }

            value.documentChanges.forEach { item ->

                when (item.type) {

                    DocumentChange.Type.ADDED -> {
                        val chat = item.document.toConversionChatItem(uid = uid)
                        onAddedChat(chat.copy(isSent = !item.document.isDeviceOffline()))
                    }

                    else -> {}
                }
            }



            value.documents.forEach { item ->
                val chat = item.toConversionChatItem(uid = uid)
                onModifiedChat(chat.copy(isSent = !item.isDeviceOffline()))
            }

        }
    }


}

