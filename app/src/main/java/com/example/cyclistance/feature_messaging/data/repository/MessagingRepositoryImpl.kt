package com.example.cyclistance.feature_messaging.data.repository

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_CHAT_COLLECTION
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_FCM_TOKEN
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_MESSAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_RECEIVER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_SENDER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_TIMESTAMP
import com.example.cyclistance.core.utils.constants.MessagingConstants.SAVED_TOKEN
import com.example.cyclistance.core.utils.constants.UtilsConstants.USER_COLLECTION
import com.example.cyclistance.core.utils.contexts.dataStore
import com.example.cyclistance.core.utils.data_store_ext.editData
import com.example.cyclistance.core.utils.data_store_ext.getData
import com.example.cyclistance.feature_messaging.data.mapper.MessagingConversationItemMapper.toConversationItem
import com.example.cyclistance.feature_messaging.data.mapper.MessagingUserItemMapper.toMessageUser
import com.example.cyclistance.feature_messaging.domain.exceptions.MessagingExceptions
import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.helper.Conversation
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.Date
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MessagingRepositoryImpl(
    private val fireStore: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging,
    private val auth: FirebaseAuth,
    private val appContext: Context,
    private val chatMessages: Conversation = Conversation(),
    private var snapShotListener: ListenerRegistration? = null
) : MessagingRepository {

    private val scope: CoroutineContext = Dispatchers.IO
    private var dataStore = appContext.dataStore

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
//    f80O4Y2BtrqIicuHTgjIYQ06AIPH -- juan id (xiaomi)


    override fun getUserUid(): String {
        return getUid()
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
                message = exception.message ?: "Token update failed")
        }
    }

    private fun checkInternetConnection() {
        if (!appContext.hasInternetConnection()) {
            throw MessagingExceptions.NetworkException(message = appContext.getString(R.string.no_internet_message))
        }
    }





    override fun removeMessageListener() {
        snapShotListener?.remove()
    }

    override fun addMessageListener(
        receiverId: String,
        onNewMessage: (ConversationsModel) -> Unit) {

        val userUid = getUid()
        chatMessages.clearMessage()

        val eventListener = { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
            if (value == null) {
                throw MessagingExceptions.ListenMessagingFailure(message = "Cannot listen to messages, value is null")
            }

            if (error != null) {
                throw MessagingExceptions.ListenMessagingFailure(
                    message = error.message ?: "Unknown error occurred")
            }

            value.documentChanges.filter { it.type == DocumentChange.Type.ADDED }
                .forEach { documentChange ->
                    val message = documentChange.document.toConversationItem()
                    chatMessages.addMessage(message)
                }

            chatMessages.getMessages().sortedBy { it.timeStamp }
            onNewMessage(
                ConversationsModel(
                    messages = chatMessages.getMessages()
                ))
        }

        snapShotListener = fireStore.collection(KEY_CHAT_COLLECTION)
            .whereIn(KEY_SENDER_ID, listOf(userUid, receiverId))
            .whereIn(KEY_RECEIVER_ID, listOf(userUid, receiverId))
            .addSnapshotListener(MetadataChanges.INCLUDE,eventListener)

    }


    override suspend fun refreshToken() {
        withContext(scope) {

            val token = getMessagingToken()
            val savedToken = dataStore.getData(key = SAVED_TOKEN, defaultValue = "").firstOrNull()

            checkInternetConnection()
            if (token == savedToken) {
                return@withContext
            }

            updateMessagingToken(token = token)
        }
    }

    override suspend fun getUsers(): MessagingUserModel {
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(USER_COLLECTION)
                    .addSnapshotListener(MetadataChanges.INCLUDE) { value: QuerySnapshot?, error: FirebaseFirestoreException? ->
                        if (value == null) {
                            throw MessagingExceptions.GetMessageUsersFailure(message = "Cannot get message users, value is null")
                        }
                        if (error != null) {
                            throw MessagingExceptions.GetMessageUsersFailure(
                                message = error.message ?: "Unknown error occurred")
                        }

                        value.documents.let { documents ->

                            val users: List<MessagingUserItemModel> = documents.map { document ->
                                document?.toMessageUser() ?: MessagingUserItemModel()
                            }

                            if (continuation.isActive) {
                                continuation.resume(MessagingUserModel(users = users))
                            }
                        }
                    }
            }
        }


    }


    override fun sendMessage(sendMessageModel: SendMessageModel) {
        checkInternetConnection()

        val uid = getUid()
        val message = mapOf(
            KEY_SENDER_ID to uid,
            KEY_RECEIVER_ID to sendMessageModel.receiverId,
            KEY_MESSAGE to sendMessageModel.message,
            KEY_TIMESTAMP to Date()
        )

        fireStore.collection(KEY_CHAT_COLLECTION).add(message).addOnSuccessListener {
            Timber.v("Message sent successfully ")
        }.addOnFailureListener {
            throw MessagingExceptions.SendMessagingFailure(message = it.message!!)
        }.addOnCanceledListener {
            Timber.e("Message sending cancelled by user")
        }

    }

    override suspend fun deleteToken() {
        checkInternetConnection()
        dataStore.editData(key = SAVED_TOKEN, value = "")
        fireStore.collection(
            USER_COLLECTION
        ).document(getUid()).update(KEY_FCM_TOKEN, FieldValue.delete()).addOnFailureListener {
            throw MessagingExceptions.TokenException(message = it.message!!)
        }
    }
}