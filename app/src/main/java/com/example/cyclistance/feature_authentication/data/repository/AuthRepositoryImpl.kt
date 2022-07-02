package com.example.cyclistance.feature_authentication.data.repository

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.cyclistance.R
import com.example.cyclistance.common.AuthConstants.DATA_STORE_PHONE_NUMBER_KEY
import com.example.cyclistance.common.AuthConstants.FACEBOOK_CONNECTION_FAILURE
import com.example.cyclistance.common.AuthConstants.USER_NOT_FOUND
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.utils.dataStore
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine



class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
) : AuthRepository<AuthCredential> {

    private var dataStore = context.dataStore


    override suspend fun uploadImage(uri: Uri): Uri {
        val firebaseStorageReference: StorageReference = FirebaseStorage.getInstance().reference
        val reference = firebaseStorageReference.child("images/${getId()}")
        val uploadTask = reference.putFile(uri)
        return suspendCoroutine { continuation ->
            uploadTask.addOnCompleteListener { task ->
                task.exception?.let(continuation::resumeWithException)
                if (task.isSuccessful) {
                    reference.downloadUrl.addOnSuccessListener(continuation::resume)
                }
            }
        }
    }

    override suspend fun reloadEmail(): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().currentUser?.reload()?.addOnCompleteListener { reload ->
                reload.exception?.let { exception ->
                    if (exception is FirebaseNetworkException) {
                        continuation.resumeWithException(
                            AuthExceptions.InternetException(
                                message = context.getString(
                                    R.string.no_internet_message)))
                    }
                }
                continuation.resume(reload.isSuccessful)
            }
        }
    }

    override suspend fun sendEmailVerification(): Boolean {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()
                ?.addOnCompleteListener { sendEmail ->
                    sendEmail.exception?.let {
                        continuation.resumeWithException(
                            AuthExceptions.EmailVerificationException(
                                message = context.getString(R.string.failed_email_verification)))
                    }
                    continuation.resume(sendEmail.isSuccessful)
                }
        }
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean {

        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password.trim())
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
                    continuation.resume(createAccount.isSuccessful)
                }
        }
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {

        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password.trim())
                .addOnCompleteListener { signInWithEmailAndPassword ->
                    signInWithEmailAndPassword.exception?.let { exception ->
                        if(exception is FirebaseNetworkException){
                            continuation.resumeWithException(AuthExceptions.InternetException(
                                message = context.getString(
                                    R.string.no_internet_message)))
                            return@addOnCompleteListener
                        }

                        if(exception is FirebaseAuthInvalidCredentialsException){
                            continuation.resumeWithException( AuthExceptions.PasswordException(
                                message = context.getString(
                                    R.string.incorrectPasswordMessage)))
                            return@addOnCompleteListener
                        }

                        if(exception is FirebaseAuthInvalidUserException){
                            if(exception.errorCode == USER_NOT_FOUND){
                                continuation.resumeWithException(AuthExceptions.EmailException(
                                    message = context.getString(
                                        R.string.couldntFindAccount)))
                                return@addOnCompleteListener
                            }
                        }

                        if (exception is FirebaseTooManyRequestsException) {
                            continuation.resumeWithException(  AuthExceptions.TooManyRequestsException(
                                title = context.getString(
                                    R.string.tooManyFailedAttempts),
                                message = context.getString(R.string.manyFailedAttempts)))
                            return@addOnCompleteListener
                        }

                        continuation.resumeWithException(exception)
                    }
                    continuation.resume(signInWithEmailAndPassword.isSuccessful)
                }
        }
    }


    override suspend fun signInWithCredentials(v: AuthCredential): Boolean {

        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().signInWithCredential(v)
                .addOnCompleteListener { signInWithCredential ->
                    signInWithCredential.exception?.let { exception ->
                        if(exception.message == FACEBOOK_CONNECTION_FAILURE){
                            continuation.resumeWithException(AuthExceptions.InternetException(
                                message = context.getString(
                                    R.string.no_internet_message)))
                        }
                        continuation.resumeWithException(AuthExceptions.ConflictFBTokenException(exception.message
                            ?: "Sorry, something went wrong. Please try again."))
                    }
                    continuation.resume(signInWithCredential.isSuccessful)
                }
        }
    }

    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun getId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun getEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }

    override fun getName(): String? {
        return FirebaseAuth.getInstance().currentUser?.displayName
    }

    override fun getPreference(): Flow<String> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                Timber.e(message = exception.localizedMessage ?: "Unexpected error occurred.")
            }
        }.map { preference ->
            preference[DATA_STORE_PHONE_NUMBER_KEY]
            ?: throw MappingExceptions.PhoneNumberException()
        }
    }


    override fun getPhotoUrl(): String {
        return FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
            .replace(oldValue = "=s96-c", newValue = "=s400-c");
    }

    override fun isSignedInWithProvider(): Flow<Boolean> = flow {
        FirebaseAuth.getInstance().currentUser?.providerData?.forEach {
            emit(
                value = (it.providerId == FacebookAuthProvider.PROVIDER_ID ||
                         it.providerId == GoogleAuthProvider.PROVIDER_ID))
        }

    }

    override fun isEmailVerified(): Boolean? {
        return FirebaseAuth.getInstance().currentUser?.isEmailVerified
    }

    override fun hasAccountSignedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    override suspend fun updatePreference(value: String) {
        dataStore.edit { preferences ->
            preferences[DATA_STORE_PHONE_NUMBER_KEY] = value
        }
    }


    override suspend fun updateProfilePicture(photoUri: Uri?, name: String?): Boolean {
        val profileUpdates = userProfileChangeRequest {
            name?.let { this.displayName = it }
            photoUri?.let { this.photoUri = photoUri }
        }

        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().currentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { updateProfile ->
                    updateProfile.exception?.let(continuation::resumeWithException)
                    continuation.resume(updateProfile.isSuccessful)
                }
        }
    }


}