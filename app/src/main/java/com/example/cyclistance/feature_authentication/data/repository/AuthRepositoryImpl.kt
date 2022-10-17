package com.example.cyclistance.feature_authentication.data.repository

import android.content.Context
import android.net.Uri
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.AuthConstants.DATA_STORE_PHONE_NUMBER_KEY
import com.example.cyclistance.core.utils.constants.AuthConstants.FACEBOOK_CONNECTION_FAILURE
import com.example.cyclistance.core.utils.constants.AuthConstants.IMAGE_LARGE_SIZE
import com.example.cyclistance.core.utils.constants.AuthConstants.IMAGE_SMALL_SIZE
import com.example.cyclistance.core.utils.constants.AuthConstants.USER_NOT_FOUND
import com.example.cyclistance.core.utils.editData
import com.example.cyclistance.core.utils.getData
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping_screen.data.repository.dataStore
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class AuthRepositoryImpl(
    private val context: Context,
    private val auth: FirebaseAuth,

    ) : AuthRepository<AuthCredential> {

    private var dataStore = context.dataStore


    override suspend fun uploadImage(uri: Uri): Uri {
        val firebaseStorageReference: StorageReference = FirebaseStorage.getInstance().reference
        val reference = firebaseStorageReference.child("images/${getId()}")
        val uploadTask = reference.putFile(uri)
        return suspendCancellableCoroutine { continuation ->
            uploadTask.addOnCompleteListener { task ->
                task.exception?.let(continuation::resumeWithException)
                if (task.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener(continuation::resume)
                }
            }
        }
    }

    override suspend fun reloadEmail(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            auth.currentUser?.reload()?.addOnCompleteListener { reload ->
                reload.exception?.let { exception ->
                    if (exception is FirebaseNetworkException) {
                        continuation.resumeWithException(
                            AuthExceptions.InternetException(
                                message = context.getString(
                                    R.string.no_internet_message)))
                    }
                }
                if (continuation.isActive) {
                    continuation.resume(reload.isSuccessful)
                }
            }
        }
    }

    override suspend fun sendEmailVerification(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { sendEmail ->
                sendEmail.exception?.let {
                    continuation.resumeWithException(
                        AuthExceptions.EmailVerificationException(message = context.getString(R.string.failed_email_verification)))
                }
                if (continuation.isActive) {
                    continuation.resume(sendEmail.isSuccessful)
                }
            }
        }
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean {

        return suspendCancellableCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                .addOnCompleteListener { createAccount ->
                    createAccount.exception?.let { exception ->
                        if (exception is FirebaseNetworkException) {
                            continuation.resumeWithException(
                                AuthExceptions.InternetException(
                                    message = context.getString(R.string.no_internet_message)))
                            return@addOnCompleteListener
                        }

                        if (exception is FirebaseAuthUserCollisionException) {
                            continuation.resumeWithException(
                                AuthExceptions.UserAlreadyExistsException(
                                    title = context.getString(R.string.userAlreadyExists),
                                    message = context.getString(R.string.accountAlreadyInUse)))
                            return@addOnCompleteListener
                        }
                        continuation.resumeWithException(exception)
                    }
                    if (continuation.isActive) {
                        continuation.resume(createAccount.isSuccessful)
                    }
                }
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {

        return suspendCancellableCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email.trim(), password.trim())
                .addOnCompleteListener { signInWithEmailAndPassword ->
                    signInWithEmailAndPassword.exception?.let { exception ->
                        Timber.e(exception.message)
                        if (exception is FirebaseNetworkException) {
                            continuation.resumeWithException(
                                AuthExceptions.InternetException(
                                    message = context.getString(
                                        R.string.no_internet_message)))
                            return@addOnCompleteListener
                        }

                        if (exception is FirebaseAuthInvalidCredentialsException) {
                            continuation.resumeWithException(
                                AuthExceptions.PasswordException(
                                    message = context.getString(
                                        R.string.incorrectPasswordMessage)))
                            return@addOnCompleteListener
                        }

                        if (exception is FirebaseAuthInvalidUserException) {
                            if (exception.errorCode == USER_NOT_FOUND) {
                                continuation.resumeWithException(
                                    AuthExceptions.EmailException(
                                        message = context.getString(
                                            R.string.couldntFindAccount)))
                                return@addOnCompleteListener
                            }
                        }

                        if (exception is FirebaseTooManyRequestsException) {
                            continuation.resumeWithException(
                                AuthExceptions.TooManyRequestsException(
                                    title = context.getString(
                                        R.string.tooManyFailedAttempts),
                                    message = context.getString(R.string.manyFailedAttempts)))
                            return@addOnCompleteListener
                        }

                        if (exception is IllegalStateException) {
                            Timber.e(exception.message)
                        }

                        continuation.resumeWithException(exception)
                    }
                    if (continuation.isActive) {
                        continuation.resume(signInWithEmailAndPassword.isSuccessful)
                    }
                }
        }
    }


    override suspend fun signInWithCredentials(v: AuthCredential): Boolean {
        return suspendCancellableCoroutine { continuation ->

            auth.signInWithCredential(v)
                .addOnCompleteListener { signInWithCredential ->
                    signInWithCredential.exception?.let { exception ->
                        if (exception.message == FACEBOOK_CONNECTION_FAILURE) {
                            continuation.resumeWithException(
                                AuthExceptions.InternetException(
                                    message = context.getString(
                                        R.string.no_internet_message)))
                        }
                        continuation.resumeWithException(
                            AuthExceptions.ConflictFBTokenException(
                                exception.message
                                ?: "Sorry, something went wrong. Please try again."))
                    }
                    if (continuation.isActive) {
                        continuation.resume(signInWithCredential.isSuccessful)
                    }
                }
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getId(): String? {
        return auth.currentUser?.uid
    }

    override fun getEmail(): String? {
        return auth.currentUser?.email
    }

    override fun getName(): String? {
        return auth.currentUser?.displayName
    }


    override fun getPhotoUrl(): String {
        return auth.currentUser?.photoUrl.toString()
            .replace(oldValue = IMAGE_SMALL_SIZE, newValue = IMAGE_LARGE_SIZE)
    }

    override fun isSignedInWithProvider(): Boolean? {
        return auth.currentUser?.providerData?.any {
            it.providerId == FacebookAuthProvider.PROVIDER_ID ||
            it.providerId == GoogleAuthProvider.PROVIDER_ID
        }
    }

    override fun isEmailVerified(): Boolean? {
        return auth.currentUser?.isEmailVerified
    }

    override fun hasAccountSignedIn(): Boolean {
        return auth.currentUser != null
    }


    override suspend fun updateProfile(photoUri: Uri?, name: String?): Boolean {
        val profileUpdates = userProfileChangeRequest {
            name?.let { this.displayName = it }
            photoUri?.let { this.photoUri = photoUri }
        }

        return suspendCancellableCoroutine { continuation ->
            auth.currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { updateProfile ->
                    updateProfile.exception?.let(continuation::resumeWithException)
                    if (continuation.isActive) {
                        continuation.resume(updateProfile.isSuccessful)
                    }
                }
        }
    }

    override suspend fun updatePhoneNumber(phoneNumber: String) {
        dataStore.editData(DATA_STORE_PHONE_NUMBER_KEY, phoneNumber)
    }

    override fun getPhoneNumber(): Flow<String> {
        return dataStore.getData(key = DATA_STORE_PHONE_NUMBER_KEY, defaultValue = "")
    }

}