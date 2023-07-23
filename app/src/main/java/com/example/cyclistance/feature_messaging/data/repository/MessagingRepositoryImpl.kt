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
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.MessagingUserItem
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.MessagingUsers
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MessagingRepositoryImpl(
    private val fireStore: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging,
    private val auth: FirebaseAuth,
    private val firebaseInstallations: FirebaseInstallations,
    private val appContext: Context,
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

    override suspend fun getUsers(): MessagingUsers {
        checkInternetConnection()
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(USER_COLLECTION).get().addOnSuccessListener {
                    it.documents.let { documents ->
                        val users: List<MessagingUserItem> = documents.map { document ->
                            document.toMessageUser()

                        }
                        continuation.resume(MessagingUsers(users = users))
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

    override suspend fun sendMessage(conversationItem: ConversationItemModel): Boolean {
        checkInternetConnection()
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                val uid = getUid()
                val message = mapOf(
                    KEY_SENDER_ID to uid,
                    KEY_RECEIVER_ID to conversationItem.receiverId,
                    KEY_MESSAGE to conversationItem.message,
                    KEY_TIMESTAMP to Date(),
                )
                fireStore.collection(KEY_CHAT_COLLECTION).add(message).addOnSuccessListener {
                    continuation.resume(true)
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