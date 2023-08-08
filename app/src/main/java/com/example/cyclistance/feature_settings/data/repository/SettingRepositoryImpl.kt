package com.example.cyclistance.feature_settings.data.repository

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.AuthConstants
import com.example.cyclistance.core.utils.constants.AuthConstants.IMAGE_LARGE_SIZE
import com.example.cyclistance.core.utils.constants.AuthConstants.IMAGE_SMALL_SIZE
import com.example.cyclistance.core.utils.constants.SettingConstants.DATA_STORE_THEME_KEY
import com.example.cyclistance.core.utils.contexts.dataStore
import com.example.cyclistance.core.utils.data_store_ext.editData
import com.example.cyclistance.core.utils.data_store_ext.getData
import com.example.cyclistance.feature_settings.domain.exceptions.SettingExceptions
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class SettingRepositoryImpl(
    val context: Context,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val scope: CoroutineContext = Dispatchers.IO

) : SettingRepository {
    private var dataStore = context.dataStore


    override suspend fun toggleTheme(value: Boolean) {
        withContext(scope) {
            dataStore.editData(DATA_STORE_THEME_KEY, value)
        }
    }

    override fun isDarkTheme(): Flow<Boolean> {
        return dataStore.getData(key = DATA_STORE_THEME_KEY, defaultValue = false)
    }

    override suspend fun updatePhoneNumber(phoneNumber: String) {
        withContext(scope) {
            dataStore.editData(AuthConstants.DATA_STORE_PHONE_NUMBER_KEY, phoneNumber)
        }
    }

    override fun getPhoneNumber(): Flow<String> {
        return dataStore.getData(key = AuthConstants.DATA_STORE_PHONE_NUMBER_KEY, defaultValue = "")
    }


    override suspend fun updateProfile(photoUri: String?, name: String?): Boolean {
        val profileUpdates = userProfileChangeRequest {
            name?.let { this.displayName = it }
            photoUri?.let { this.photoUri = Uri.parse(photoUri) }
        }


        if (!context.hasInternetConnection()) {
            throw SettingExceptions.NetworkException(message = context.getString(R.string.no_internet_message))
        }

        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                auth.currentUser?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { updateProfile ->
                        updateProfile.exception?.let { exception ->

                            if (exception is FirebaseNetworkException) {
                                continuation.resumeWithException(
                                    SettingExceptions.NetworkException(
                                        message = context.getString(R.string.no_internet_message)))
                            }

                            if (exception is FirebaseException) {

                                continuation.resumeWithException(
                                    SettingExceptions.InternalServerException(
                                        message = exception.message
                                                  ?: context.getString(R.string.somethingWentWrong))
                                )
                            }

                        }
                        if (continuation.isActive) {
                            continuation.resume(updateProfile.isSuccessful)
                        }
                    }
            }
        }
    }


    override suspend fun uploadImage(v: String): String {
        val id = auth.currentUser?.uid
        val reference = storage.reference.child("images/${id}")
        val uploadTask = reference.putFile(Uri.parse(v))
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                uploadTask.addOnCompleteListener { task ->
                    task.exception?.let { exception ->
                        if (exception is FirebaseNetworkException) {
                            continuation.resumeWithException(
                                SettingExceptions.NetworkException(
                                    message = context.getString(R.string.no_internet_message)))
                        }
                    }

                    if (task.isSuccessful && continuation.isActive) {
                        reference.downloadUrl.addOnSuccessListener {
                            continuation.resume(it.toString())
                        }
                    }

                }
            }
        }
    }

    private fun getFacebookToken(): AccessToken? {
        return AccessToken.getCurrentAccessToken()
    }

    private suspend fun getUserFacebookInformation(): String? {
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                val request =
                    GraphRequest.newMeRequest(getFacebookToken()) { jsonObject: JSONObject?, _ ->
                        val result = jsonObject.toString().takeUnless { it == "null" }
                        if (continuation.isActive) {
                            continuation.resume(result)
                        }
                    }
                val parameters = Bundle()
                parameters.putString("fields", "id,name, email, link, picture.type(large)")
                request.parameters = parameters
                request.executeAsync()
            }
        }
    }


    override suspend fun getName(): String? {
        return withContext(scope) {
            val infoString = getUserFacebookInformation()
            val jsonObject = infoString?.let(::JSONObject)
            val fbName = jsonObject?.getString("name")
            val authName: String? = auth.currentUser?.displayName
            val authEmail: String? = auth.currentUser?.email
            val extractedNameFromEmail = authEmail?.substringBefore("@")
            val possibleName = authName?.takeIf { it.isNotEmpty() } ?: fbName
            possibleName.takeIf { !it.isNullOrEmpty() } ?: extractedNameFromEmail
        }

    }

    override suspend fun getPhotoUrl(): String? {
        return withContext(scope) {
            val infoString = getUserFacebookInformation()
            val jsonObject = infoString?.let(::JSONObject)
            val fbPhotoUrl =
                jsonObject?.getJSONObject("picture")?.getJSONObject("data")?.getString("url")
            val authPhotoUrl: String = auth.currentUser?.photoUrl.toString().apply {
                replace(oldValue = IMAGE_SMALL_SIZE, newValue = IMAGE_LARGE_SIZE)
            }
            authPhotoUrl.takeIf { it.isNotEmpty() } ?: fbPhotoUrl
        }

    }
}