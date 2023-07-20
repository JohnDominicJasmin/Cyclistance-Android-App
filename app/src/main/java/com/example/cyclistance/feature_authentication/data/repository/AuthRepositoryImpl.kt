package com.example.cyclistance.feature_authentication.data.repository

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.connection.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.core.utils.constants.AuthConstants.FACEBOOK_CONNECTION_FAILURE
import com.example.cyclistance.core.utils.constants.AuthConstants.USER_DOCUMENT
import com.example.cyclistance.core.utils.constants.AuthConstants.USER_NOT_FOUND
import com.example.cyclistance.feature_authentication.data.mapper.AuthenticationUserMapper.toAuthenticationUser
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthenticationResult
import com.example.cyclistance.feature_authentication.domain.model.AuthenticationUser
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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


    override suspend fun createUser(authUser: AuthenticationUser) {

        checkInternetConnection()

        fireStore.document("$USER_DOCUMENT/${authUser.uid}").set(authUser)
            .addOnCompleteListener { task ->
                task.exception?.let {

                    if (it is FirebaseNetworkException) {
                        throw AuthExceptions.NetworkException(
                            message = appContext.getString(
                                R.string.no_internet_message))
                    }

                    throw AuthExceptions.CreateUserException(
                        message = appContext.getString(R.string.failed_create_user))
                }

                if (!task.isSuccessful) {
                    throw AuthExceptions.CreateUserException(
                        message = appContext.getString(R.string.failed_create_user))
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

                            continuation.resume(
                                AuthenticationResult(
                                    isSuccessful = task.isSuccessful,
                                    authUser = task.result.user!!.toAuthenticationUser()
                                ))
                        }
                    }
            }
        }
    }


    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String): AuthenticationResult? {

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
                            continuation.resume(
                                AuthenticationResult(
                                    isSuccessful = task.isSuccessful,
                                    authUser = task.result.user!!.toAuthenticationUser()
                                ))
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


    private fun CancellableContinuation<AuthenticationResult?>.handleSignInWithEmailAndPasswordException(
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
                AuthExceptions.PasswordException(
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

    override suspend fun signInWithCredential(credential: SignInCredential): Boolean {
        return withContext(scope) {
            suspendCancellableCoroutine { continuation ->

                val signInCredential = when (credential) {
                    is SignInCredential.Google -> GoogleAuthProvider.getCredential(
                        credential.providerToken,
                        null)

                    is SignInCredential.Facebook -> FacebookAuthProvider.getCredential(credential.providerToken)
                }


                auth.signInWithCredential(signInCredential)
                    .addOnCompleteListener { signInWithCredential ->
                        signInWithCredential.exception?.let { exception ->
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
                            continuation.resume(signInWithCredential.isSuccessful)
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


}