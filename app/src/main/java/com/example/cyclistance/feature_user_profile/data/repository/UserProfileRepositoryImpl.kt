package com.example.cyclistance.feature_user_profile.data.repository

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.AuthConstants
import com.example.cyclistance.core.utils.constants.UserProfileConstants.KEY_ADDRESS
import com.example.cyclistance.core.utils.constants.UserProfileConstants.KEY_BIKE_GROUP
import com.example.cyclistance.core.utils.constants.UserProfileConstants.KEY_USER_ACTIVITY
import com.example.cyclistance.core.utils.constants.UserProfileConstants.KEY_USER_REASON_ASSISTANCE
import com.example.cyclistance.core.utils.constants.UtilConstants
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_NAME
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_PHOTO
import com.example.cyclistance.feature_user_profile.data.mapper.UserProfileInfoMapper.toUserProfileInfo
import com.example.cyclistance.feature_user_profile.domain.exceptions.UserProfileExceptions
import com.example.cyclistance.feature_user_profile.domain.model.ReasonAssistanceModel
import com.example.cyclistance.feature_user_profile.domain.model.UserActivityModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel
import com.example.cyclistance.feature_user_profile.domain.repository.UserProfileRepository
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserProfileRepositoryImpl(
    val context: Context,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val fireStore: FirebaseFirestore,
    private val scope: CoroutineContext = Dispatchers.IO
) : UserProfileRepository {


    override suspend fun updateProfile(photoUri: String?, name: String?): Boolean {
        val profileUpdates = userProfileChangeRequest {
            name?.let { this.displayName = it }
            photoUri?.let { this.photoUri = Uri.parse(photoUri) }
        }


        if (!context.hasInternetConnection()) {
            throw UserProfileExceptions.NetworkException(message = context.getString(R.string.no_internet_message))
        }

        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                auth.currentUser?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { updateProfile ->
                        updateProfile.exception?.let { exception ->

                            if (exception is FirebaseNetworkException) {
                                continuation.resumeWithException(
                                    UserProfileExceptions.NetworkException(
                                        message = context.getString(R.string.no_internet_message)))
                            }

                            if (exception is FirebaseException) {

                                continuation.resumeWithException(
                                    UserProfileExceptions.InternalServerException(
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
                                UserProfileExceptions.NetworkException(
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
                replace(
                    oldValue = AuthConstants.IMAGE_SMALL_SIZE,
                    newValue = AuthConstants.IMAGE_LARGE_SIZE)
            }
            authPhotoUrl.takeIf { it.isNotEmpty() } ?: fbPhotoUrl
        }

    }

    override suspend fun updateUserActivity(id: String, userActivity: UserActivityModel) {

        suspendCancellableCoroutine { continuation ->
            fireStore.collection(UtilConstants.USER_COLLECTION).document(id)
                .update(
                    mapOf(KEY_USER_ACTIVITY to userActivity)
                ).addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(
                        UserProfileExceptions.UpdateActivityException(
                            it.message!!))
                }
        }
    }

    override suspend fun updateReasonAssistance(
        id: String,
        reasonAssistanceModel: ReasonAssistanceModel) {

        suspendCancellableCoroutine { continuation ->
            fireStore.collection(UtilConstants.USER_COLLECTION).document(id)
                .update(
                    mapOf(
                        KEY_USER_REASON_ASSISTANCE to reasonAssistanceModel
                    )
                ).addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(
                        UserProfileExceptions.UpdateReasonAssistanceException(
                            it.message!!))
                }
        }
    }



    override suspend fun updateUserProfileInfo(id: String, userProfile: UserProfileInfoModel) {

        suspendCancellableCoroutine { continuation ->
            fireStore.collection(UtilConstants.USER_COLLECTION).document(id)
                .update(
                    KEY_NAME, userProfile.name,
                    KEY_PHOTO, userProfile.photoUrl,
                    KEY_ADDRESS, userProfile.address,
                    KEY_BIKE_GROUP, userProfile.bikeGroup,
                ).addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(UserProfileExceptions.UpdateProfileException(it.message!!))
                }
        }
    }


    override suspend fun getUserProfile(id: String): UserProfileModel {


        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                fireStore.collection(UtilConstants.USER_COLLECTION).document(id)
                    .get().addOnSuccessListener { documentSnapshot ->
                        continuation.resume(documentSnapshot.toUserProfileInfo())


                    }.addOnFailureListener {
                        continuation.resumeWithException(
                            UserProfileExceptions.GetProfileException(
                                it.message!!))
                    }
            }
        }
    }

}