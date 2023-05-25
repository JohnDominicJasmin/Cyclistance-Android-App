package com.example.cyclistance.feature_settings.data.repository

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.AuthConstants
import com.example.cyclistance.core.utils.constants.SettingConstants.DATA_STORE_THEME_KEY
import com.example.cyclistance.core.utils.extension.editData
import com.example.cyclistance.core.utils.extension.getData
import com.example.cyclistance.feature_mapping.data.repository.dataStore
import com.example.cyclistance.feature_settings.domain.exceptions.SettingExceptions
import com.example.cyclistance.feature_settings.domain.repository.SettingRepository
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class SettingRepositoryImpl(
    val context: Context,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage

) : SettingRepository {
    private var dataStore = context.dataStore


    override suspend fun toggleTheme(value: Boolean) {
        dataStore.editData(DATA_STORE_THEME_KEY, value)
    }

    override fun isDarkTheme(): Flow<Boolean> {
        return dataStore.getData(key = DATA_STORE_THEME_KEY, defaultValue = false)
    }

    override suspend fun updatePhoneNumber(phoneNumber: String) {
        dataStore.editData(AuthConstants.DATA_STORE_PHONE_NUMBER_KEY, phoneNumber)
    }

    override fun getPhoneNumber(): Flow<String> {
        return dataStore.getData(key = AuthConstants.DATA_STORE_PHONE_NUMBER_KEY, defaultValue = "")
    }


    override suspend fun updateProfile(photoUrl: String?, name: String?): Boolean {
        val profileUpdates = userProfileChangeRequest {
            name?.let { this.displayName = it }
            photoUrl?.let { this.photoUri = Uri.parse(photoUrl) }
        }


        if (!context.hasInternetConnection()) {
            throw SettingExceptions.NetworkException(message = context.getString(R.string.no_internet_message))
        }

        return suspendCancellableCoroutine { continuation ->
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


    override suspend fun uploadImage(v: String): String {
        val id = auth.currentUser?.uid
        val reference = storage.reference.child("images/${id}")
        val uploadTask = reference.putFile(Uri.parse(v))
        return suspendCancellableCoroutine { continuation ->
            uploadTask.addOnCompleteListener { task ->
                task.exception?.let { exception ->
                    if (exception is FirebaseNetworkException) {
                        continuation.resumeWithException(
                            SettingExceptions.NetworkException(
                                message = context.getString(R.string.no_internet_message)))
                    }
                }

                if (task.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener{
                        continuation.resume(it.toString())
                    }
                }

            }
        }
    }

    private fun getFacebookToken(): AccessToken?{
        return AccessToken.getCurrentAccessToken()
    }

    private suspend fun getUserFacebookInformation(): String{
        return suspendCancellableCoroutine { continuation ->
            val request = GraphRequest.newMeRequest(getFacebookToken()) { jsonObject: JSONObject?, _ ->
                continuation.resume(jsonObject.toString())
            }
            val parameters = Bundle()
            parameters.putString("fields", "id,name, email, link, picture.type(large)")
            request.parameters = parameters
            request.executeAsync()
        }
    }


    override suspend fun getName(): String? {
        val infoString = getUserFacebookInformation()
        val jsonObject = JSONObject(infoString)
        val fbName = jsonObject.getString("name")
        return auth.currentUser?.displayName?.takeIf { it.isNotEmpty() } ?: fbName
    }

    override suspend fun getPhotoUrl(): String {
        val infoString = getUserFacebookInformation()
        val jsonObject = JSONObject(infoString)
        val fbPhotoUrl = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url")
        return auth.currentUser?.photoUrl.toString().replace(oldValue = AuthConstants.IMAGE_SMALL_SIZE, newValue = AuthConstants.IMAGE_LARGE_SIZE).takeIf { it.isNotEmpty() } ?: fbPhotoUrl

    }
}