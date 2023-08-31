package com.example.cyclistance.feature_authentication.data.repository

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.AuthConstants.FACEBOOK_CONNECTION_FAILURE
import com.example.cyclistance.core.utils.constants.AuthConstants.USER_NOT_FOUND
import com.example.cyclistance.core.utils.constants.UtilConstants.USER_COLLECTION
import com.example.cyclistance.feature_authentication.data.mapper.AuthResultMapper.toAuthenticationResult
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthenticationResult
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class AuthRepositoryImpl(
    private val appContext: Context,
    private val auth: FirebaseAuth,
    private val fireStore: FirebaseFirestore,
    private val scope: CoroutineContext = Dispatchers.IO
) : AuthRepository {


    override suspend fun createUser(user: UserDetails) {

        checkInternetConnection()

        suspendCancellableCoroutine { continuation ->
            fireStore.document("${USER_COLLECTION}/${user.uid}").set(user).addOnSuccessListener {
                Timber.v("User created successfully")
                continuation.resume(Unit)
            }.addOnFailureListener { exception ->

                if (exception is FirebaseNetworkException) {
                    continuation.resumeWithException(
                        AuthExceptions.NetworkException(
                            message = appContext.getString(
                                R.string.no_internet_message)))
                }

                continuation.resumeWithException(
                    AuthExceptions.CreateUserException(
                        message = appContext.getString(R.string.failed_create_user)))
            }

        }
    }

    private fun checkInternetConnection() {
        if (!appContext.hasInternetConnection()) {
            throw AuthExceptions.NetworkException(message = appContext.getString(R.string.no_internet_message))
        }
    }

    override suspend fun reloadEmail(): Boolean {
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                auth.currentUser?.reload()?.addOnCompleteListener { reload ->
                    reload.exception?.let { exception ->
                        if (exception is FirebaseNetworkException) {
                            continuation.resumeWithException(
                                AuthExceptions.NetworkException(
                                    message = appContext.getString(
                                        R.string.no_internet_message)))
                        }
                    }
                    if (continuation.isActive) {
                        continuation.resume(reload.isSuccessful)
                    }
                }
            }
        }
    }

    override suspend fun sendEmailVerification(): Boolean {
        return withContext(scope) {

            suspendCancellableCoroutine { continuation ->
                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { sendEmail ->
                    sendEmail.exception?.let {
                        continuation.resumeWithException(
                            AuthExceptions.EmailVerificationException(
                                message = appContext.getString(
                                    R.string.failed_email_verification)))
                    }
                    if (continuation.isActive) {
                        continuation.resume(sendEmail.isSuccessful)
                    }
                }
            }
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String): AuthenticationResult? {

        checkInternetConnection()

        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                auth.createUserWithEmailAndPassword(email.trim(), password.trim())
                    .addOnCompleteListener { task ->
                        task.exception?.let { exception ->
                            continuation.handleCreateUserWithEmailAndPassword(exception)
                            return@addOnCompleteListener
                        }
                        if (continuation.isActive) {

                            continuation.resume(task.toAuthenticationResult())
                        }
                    }
            }
        }
    }


    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String): Boolean {

        checkInternetConnection()

        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->
                auth.signInWithEmailAndPassword(email.trim(), password.trim())
                    .addOnCompleteListener { task ->

                        task.exception?.let {
                            continuation.handleSignInWithEmailAndPasswordException(exception = it)
                            return@addOnCompleteListener
                        }

                        if (continuation.isActive) {
                            continuation.resume(task.isSuccessful)
                        }
                    }
            }
        }
    }


    private fun CancellableContinuation<AuthenticationResult?>.handleCreateUserWithEmailAndPassword(
        exception: Exception) {
        if (exception is FirebaseNetworkException) {
            resumeWithException(
                AuthExceptions.NetworkException(
                    message = appContext.getString(
                        R.string.no_internet_message)))
            return
        }

        if (exception is FirebaseAuthUserCollisionException) {
            resumeWithException(
                AuthExceptions.UserAlreadyExistsException(
                    title = appContext.getString(R.string.userAlreadyExists),
                    message = appContext.getString(R.string.accountAlreadyInUse)))
            return
        }
        resumeWithException(exception)
    }


    private fun CancellableContinuation<Boolean>.handleSignInWithEmailAndPasswordException(
        exception: Exception) {
        if (exception is FirebaseNetworkException) {
            resumeWithException(
                AuthExceptions.NetworkException(
                    message = appContext.getString(
                        R.string.no_internet_message)))
            return
        }

        if (exception is FirebaseAuthInvalidCredentialsException) {
            resumeWithException(
                AuthExceptions.NewPasswordException(
                    message = appContext.getString(
                        R.string.incorrectPasswordMessage)))
            return
        }

        if (exception is FirebaseAuthInvalidUserException) {
            if (exception.errorCode == USER_NOT_FOUND) {
                resumeWithException(
                    AuthExceptions.EmailException(
                        message = appContext.getString(
                            R.string.couldntFindAccount)))
                return
            }
        }

        if (exception is FirebaseTooManyRequestsException) {
            resumeWithException(
                AuthExceptions.TooManyRequestsException(
                    title = appContext.getString(
                        R.string.tooManyFailedAttempts),
                    message = appContext.getString(R.string.manyFailedAttempts)))
            return
        }

        if (exception is IllegalStateException) {
            Timber.e(exception.message)
        }

        resumeWithException(exception)
    }

    override suspend fun signInWithCredential(credential: SignInCredential): AuthenticationResult {
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->

                val signInCredential = when (credential) {
                    is SignInCredential.Google -> GoogleAuthProvider.getCredential(
                        credential.providerToken,
                        null)

                    is SignInCredential.Facebook -> FacebookAuthProvider.getCredential(credential.providerToken)
                }


                auth.signInWithCredential(signInCredential)
                    .addOnCompleteListener { task ->

                        task.exception?.let { exception ->
                            if (exception.message == FACEBOOK_CONNECTION_FAILURE) {
                                continuation.resumeWithException(
                                    AuthExceptions.NetworkException(
                                        message = appContext.getString(
                                            R.string.no_internet_message)))
                            }

                            if (exception is FirebaseNetworkException) {
                                continuation.resumeWithException(
                                    AuthExceptions.NetworkException(
                                        message = appContext.getString(R.string.no_internet_message)))
                                return@addOnCompleteListener
                            }

                            continuation.resumeWithException(
                                AuthExceptions.ConflictFBTokenException(
                                    exception.message
                                    ?: "Sorry, something went wrong. Please try again."))
                        }
                        if (continuation.isActive) {
                            continuation.resume(task.toAuthenticationResult())
                        }
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

    override suspend fun sendPasswordResetEmail(email: String) {
        checkInternetConnection()
        suspendCancellableCoroutine { continuation ->
            auth.sendPasswordResetEmail(email).addOnSuccessListener {
                continuation.resume(Unit)
            }.addOnFailureListener { exception ->
                if (exception is FirebaseNetworkException) {
                    continuation.resumeWithException(
                        AuthExceptions.NetworkException(
                            message = appContext.getString(
                                R.string.no_internet_message)))
                }
                if (exception is FirebaseAuthInvalidUserException) {
                    if (exception.errorCode == USER_NOT_FOUND) {
                        continuation.resumeWithException(
                            AuthExceptions.EmailException(
                                message = appContext.getString(
                                    R.string.couldntFindAccount)))
                    }
                }
            }
        }
    }

    override suspend fun changePassword(currentPassword: String, confirmPassword: String) {
        checkInternetConnection()


        val user = auth.currentUser
        val email = auth.currentUser?.email
        val credential = EmailAuthProvider.getCredential(email!!, currentPassword)

        suspendCancellableCoroutine { continuation ->

            user?.reauthenticate(credential)?.addOnSuccessListener {
                user.updatePassword(confirmPassword).addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener { exception ->

                    if (exception is FirebaseAuthWeakPasswordException) {
                        continuation.resumeWithException(
                            AuthExceptions.ConfirmPasswordException(
                                message = exception.message
                                          ?: "Password must be at least 8 characters long."))
                    }

                    if (exception is FirebaseAuthInvalidUserException) {
                        if (exception.errorCode == USER_NOT_FOUND) {
                            continuation.resumeWithException(
                                AuthExceptions.CurrentPasswordException(
                                    message = appContext.getString(
                                        R.string.couldntFindAccount)))
                        }
                    }


                }
            }?.addOnFailureListener { exception ->

                if (exception is FirebaseAuthInvalidUserException) {
                    if (exception.errorCode == USER_NOT_FOUND) {
                        continuation.resumeWithException(
                            AuthExceptions.CurrentPasswordException(
                                message = appContext.getString(
                                    R.string.couldntFindAccount)))
                    }
                }


                if (exception is FirebaseAuthInvalidCredentialsException) {
                    continuation.resumeWithException(
                        AuthExceptions.CurrentPasswordException(
                            message = appContext.getString(
                                R.string.incorrectPasswordMessage)))
                }

            }

        }

    }
}