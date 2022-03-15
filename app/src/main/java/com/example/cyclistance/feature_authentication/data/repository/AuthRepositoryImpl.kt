package com.example.cyclistance.feature_authentication.data.repository

import android.content.Context
import com.example.cyclistance.R
import com.example.cyclistance.common.AuthConstants.FB_CONNECTION_FAILURE
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AuthRepositoryImpl @Inject constructor(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private var firebaseUser: FirebaseUser? = firebaseAuth.currentUser
) : AuthRepository<AuthCredential> {


    override suspend fun reloadEmail(): Boolean {
        return suspendCoroutine { continuation ->
            firebaseUser?.reload()?.addOnCompleteListener { reload ->
                reload.exception?.let { exception ->
                    if (exception is FirebaseNetworkException) {
                        throw AuthExceptions.NoInternetException(message = context.getString(R.string.no_internet_message))
                    }
                }
                continuation.resume(reload.isSuccessful)
            }
        }
    }
    override suspend fun sendEmailVerification(): Boolean {
        return suspendCoroutine { continuation ->
            firebaseUser?.sendEmailVerification()?.addOnCompleteListener { sendEmail ->
                sendEmail.exception?.let {
                    throw AuthExceptions.EmailVerificationException(message = context.getString(R.string.failed_email_verification))
                }
                continuation.resume(sendEmail.isSuccessful)
            }
        }
    }
    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean {
        return suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { createAccount ->
                    createAccount.exception?.let { exception ->
                        if (exception is FirebaseNetworkException) {
                            throw AuthExceptions.NoInternetException(message = context.getString(R.string.no_internet_message))
                        } else {
                            throw exception
                        }
                    }

                    continuation.resume(createAccount.isSuccessful)

                }
        }
    }
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {

        return suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { signInWithEmailAndPassword ->
                    signInWithEmailAndPassword.exception?.let { exception ->
                        if (exception is FirebaseNetworkException) {
                            throw AuthExceptions.NoInternetException(message = context.getString(R.string.no_internet_message))
                        }
                        if (exception is FirebaseAuthInvalidUserException) {
                            throw AuthExceptions.InvalidUserException(message = context.getString(R.string.incorrectEmailOrPasswordMessage))
                        }
                        throw exception
                    }
                    continuation.resume(signInWithEmailAndPassword.isSuccessful)
                }
        }
    }
    override suspend fun signInWithCredentials(v: AuthCredential): Boolean {
        return suspendCoroutine { continuation ->
            firebaseAuth.signInWithCredential(v).addOnCompleteListener { signInWithCredential ->
                signInWithCredential.exception?.let { exception ->
                    if (exception.message == FB_CONNECTION_FAILURE) {
                        throw AuthExceptions.NoInternetException(message = context.getString(R.string.no_internet_message))
                    } else {
                        throw AuthExceptions.ConflictFBTokenException("// todo Remove existing fb token to refresh")
                    }
                }
                continuation.resume(signInWithCredential.isSuccessful)
            }
        }
    }
    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun registerAccount() {
        firebaseUser = firebaseAuth.currentUser
    }


    @Throws(NullPointerException::class)
    override  fun getEmail(): String {
        return firebaseUser?.email!!
    }

    @Throws(NullPointerException::class)
    override  fun getName(): String {
        return firebaseUser?.displayName!!
    }

    @Throws(NullPointerException::class)
    override suspend fun isSignedInWithProvider(id: String): Flow<Boolean> = flow {
        firebaseUser?.providerData?.forEach {
            emit(value = it.providerId == id)
        }

    }

    @Throws(NullPointerException::class)
    override fun isEmailVerified(): Boolean {
        return firebaseUser?.isEmailVerified!!
    }

    override fun hasAccountSignedIn(): Boolean {
        return firebaseUser != null
    }


}