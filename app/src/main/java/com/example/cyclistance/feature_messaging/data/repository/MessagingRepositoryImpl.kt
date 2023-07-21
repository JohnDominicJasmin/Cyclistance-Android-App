package com.example.cyclistance.feature_messaging.data.repository

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_FCM_TOKEN
import com.example.cyclistance.core.utils.constants.MessagingConstants.SAVED_TOKEN
import com.example.cyclistance.core.utils.constants.UtilsConstants.USER_COLLECTION
import com.example.cyclistance.core.utils.contexts.dataStore
import com.example.cyclistance.core.utils.data_store_ext.editData
import com.example.cyclistance.core.utils.data_store_ext.getData
import com.example.cyclistance.feature_messaging.data.mapper.MessagingUserDetailsMapper.toMessageUser
import com.example.cyclistance.feature_messaging.domain.exceptions.MessagingExceptions
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.MessagingUserItem
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.MessagingUsers
import com.example.cyclistance.feature_messaging.domain.repository.MessagingRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MessagingRepositoryImpl(
    private val fireStore: FirebaseFirestore,
    private val firebaseMessaging: FirebaseMessaging,
    private val auth: FirebaseAuth,
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

    private fun updateMessagingToken(token: String) {
        val uid = getUid()


        fireStore.collection(USER_COLLECTION).document(uid).update(
            KEY_FCM_TOKEN, token
        ).addOnSuccessListener {
            Timber.v("Messaging token updated successfully")
        }.addOnFailureListener {
            throw MessagingExceptions.TokenException(message = it.message!!)
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
            dataStore.editData(key = SAVED_TOKEN, value = token)
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
}