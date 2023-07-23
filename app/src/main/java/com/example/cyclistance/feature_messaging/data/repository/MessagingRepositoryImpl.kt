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
import com.example.cyclistance.feature_messaging.data.mapper.MessagingUserDetailsMapper.toMessageUser
import com.example.cyclistance.feature_messaging.domain.exceptions.MessagingExceptions
import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.helper.Conversation
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.UserMessageItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.UserMessagesModel
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
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
    private val chatMessages: Conversation = Conversation()
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


    private fun handleDocumentChange(document: DocumentSnapshot) {
        val messageId = document.id
        val senderId =
            document.getString(KEY_SENDER_ID) ?: throw MessagingExceptions.ListenMessagingFailure(
                message = "Sender id not found")
        val receiverId =
            document.getString(KEY_RECEIVER_ID) ?: throw MessagingExceptions.ListenMessagingFailure(
                message = "Receiver id not found")
        val messageText =
            document.getString(KEY_MESSAGE) ?: throw MessagingExceptions.ListenMessagingFailure(
                message = "Message not found")
        val timeStamp =
            document.getDate(KEY_TIMESTAMP) ?: throw MessagingExceptions.ListenMessagingFailure(
                message = "Timestamp not found")

        val message = ConversationItemModel(
            messageId = messageId,
            senderId = senderId,
            receiverId = receiverId,
            message = messageText,
            timeStamp = timeStamp
        )
        chatMessages.addMessage(message)
    }


    override fun listenForMessages(receiverId: String) {
        checkInternetConnection()

        val userUid = getUid()

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
                    val document = documentChange.document
                    handleDocumentChange(document)
                }

            chatMessages.getMessages().sortedBy { it.timeStamp }
            Timber.d("Messages: ${chatMessages.getMessages()}")
        }

        fireStore.collection(KEY_CHAT_COLLECTION)
            .whereIn(KEY_SENDER_ID, listOf(userUid, receiverId))
            .whereIn(KEY_RECEIVER_ID, listOf(userUid, receiverId))
            .addSnapshotListener(eventListener)
    }


    /*
        override fun listenForMessages(receiverId: String) {
            val userUid = getUid()
            val eventListener = { value: QuerySnapshot?, error: FirebaseFirestoreException? ->


                if (value == null) {
                    throw MessagingExceptions.ListenMessagingFailure(message = "Cannot listen to messages, value is null")
                }
                if (error != null) {
                    throw MessagingExceptions.ListenMessagingFailure(message = error.message!!)
                }

                if (value.documentChanges.isNotEmpty()) {
                    value.documentChanges.forEach { documentChange ->
                        if (documentChange.type != DocumentChange.Type.ADDED) {
                            return@forEach
                        }

                        val document = documentChange.document
                        val message = ConversationItemModel(
                            messageId = document.id,
                            senderId = document.getString(KEY_SENDER_ID)
                                       ?: throw MessagingExceptions.ListenMessagingFailure(message = "Sender id not found"),
                            receiverId = document.getString(KEY_RECEIVER_ID)
                                         ?: throw MessagingExceptions.ListenMessagingFailure(message = "Receiver id not found"),
                            message = document.getString(KEY_MESSAGE)
                                      ?: throw MessagingExceptions.ListenMessagingFailure(message = "Message not found"),
                            timeStamp = document.getDate(KEY_TIMESTAMP)
                                        ?: throw MessagingExceptions.ListenMessagingFailure(message = "Timestamp not found")

                        )
                        chatMessages.add(message)
                    }

                    chatMessages.sortWith { first, second ->
                        first.timeStamp!!.compareTo(second.timeStamp!!)
                    }
                }
            }

            fireStore.collection(KEY_CHAT_COLLECTION)
                .whereEqualTo(KEY_SENDER_ID, userUid)
                .whereEqualTo(KEY_RECEIVER_ID, receiverId)
                .addSnapshotListener(eventListener)

            fireStore.collection(KEY_CHAT_COLLECTION)
                .whereEqualTo(KEY_SENDER_ID, receiverId)
                .whereEqualTo(KEY_RECEIVER_ID, userUid)
                .addSnapshotListener(eventListener)
        }
    */

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

    override suspend fun getUsers(): UserMessagesModel {
        checkInternetConnection()
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(USER_COLLECTION).get().addOnSuccessListener {
                    it.documents.let { documents ->
                        val users: List<UserMessageItemModel> = documents.map { document ->
                            document.toMessageUser()

                        }

                        if (continuation.isActive) {
                            continuation.resume(UserMessagesModel(users = users))
                        }
                    }
                }.addOnFailureListener {
                    if (it is FirebaseNetworkException) {
                        continuation.resumeWithException(
                            MessagingExceptions.NetworkException(
                                message = it.message!!))
                    }
                }
            }
        }


    }

    override suspend fun sendMessage(sendMessageModel: SendMessageModel): Boolean {
        checkInternetConnection()
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                val uid = getUid()
                val message = mapOf(
                    KEY_SENDER_ID to uid,
                    KEY_RECEIVER_ID to sendMessageModel.receiverId,
                    KEY_MESSAGE to sendMessageModel.message,
                    KEY_TIMESTAMP to Date(),
                )
                fireStore.collection(KEY_CHAT_COLLECTION).add(message).addOnSuccessListener {
                    if (continuation.isActive) {
                        continuation.resume(true)
                    }
                }.addOnFailureListener {
                    continuation.resumeWithException(
                        MessagingExceptions.SendMessagingFailure(
                            message = it.message!!
                        )
                    )
                }
            }
        }

    }

    override fun deleteToken() {
        checkInternetConnection()
        fireStore.collection(
            USER_COLLECTION
        ).document(getUid()).update(KEY_FCM_TOKEN, FieldValue.delete()).addOnFailureListener {
            throw MessagingExceptions.TokenException(message = it.message!!)
        }
    }
}