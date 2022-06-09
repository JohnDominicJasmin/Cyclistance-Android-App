package com.example.cyclistance.feature_authentication.data.repository

import android.content.Context
import android.net.Uri
import com.example.cyclistance.R
import com.example.cyclistance.common.AuthConstants.FB_CONNECTION_FAILURE
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject



class AuthRepositoryImpl @Inject constructor(
    private val context: Context
) : AuthRepository<AuthCredential> {


    override suspend fun reloadEmail(): Boolean {
        return CompletableDeferred<Boolean>().run {
            FirebaseAuth.getInstance().currentUser?.reload()?.addOnCompleteListener { reload ->
                reload.exception?.let { exception ->
                    if (exception is FirebaseNetworkException) {
                        this.completeExceptionally(AuthExceptions.InternetException(message = context.getString(R.string.no_internet_message)))
                    }
                }
                this.complete(reload.isSuccessful)
            }
            this.await()
        }
    }
    override suspend fun sendEmailVerification(): Boolean {
        return CompletableDeferred<Boolean>().run {
            FirebaseAuth.getInstance().currentUser?.sendEmailVerification()?.addOnCompleteListener { sendEmail ->
                sendEmail.exception?.let {
                    this.completeExceptionally(AuthExceptions.EmailVerificationException(message = context.getString(R.string.failed_email_verification)))
                }
                this.complete(sendEmail.isSuccessful)
            }
            this.await()
        }
    }
    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean {
        return CompletableDeferred<Boolean>().run {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.trim(), password.trim())
                .addOnCompleteListener { createAccount ->
                    createAccount.exception?.let { exception ->
                        if (exception is FirebaseNetworkException) {
                            this.completeExceptionally(AuthExceptions.InternetException(message = context.getString(R.string.no_internet_message)))
                            return@addOnCompleteListener
                        }
                        if(exception is FirebaseAuthUserCollisionException){
                            this.completeExceptionally(AuthExceptions.UserAlreadyExistsException(title = context.getString(R.string.userAlreadyExists) ,message = context.getString(R.string.accountAlreadyInUse)))
                            return@addOnCompleteListener
                        }
                        this.completeExceptionally(exception)

                    }
                    this.complete(createAccount.isSuccessful)
                }
            this.await()
        }
    }
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {
        return CompletableDeferred<Boolean>().run {

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.trim(), password.trim())
                .addOnCompleteListener { signInWithEmailAndPassword ->
                    signInWithEmailAndPassword.exception?.let { exception ->

                        if (exception is FirebaseNetworkException) {
                            this.completeExceptionally(AuthExceptions.InternetException(message = context.getString(R.string.no_internet_message)))
                            return@addOnCompleteListener
                        }
                        if (exception is FirebaseAuthInvalidCredentialsException) {
                            this.completeExceptionally(AuthExceptions.PasswordException(message = context.getString(R.string.incorrectPasswordMessage)))
                            return@addOnCompleteListener
                        }
                        if(exception is FirebaseAuthInvalidUserException) {
                            if (exception.errorCode == "ERROR_USER_NOT_FOUND") {
                                this.completeExceptionally(AuthExceptions.EmailException(message = context.getString(R.string.couldntFindAccount)))
                                return@addOnCompleteListener
                            }
                        }
                        if(exception is FirebaseTooManyRequestsException ){
                            this.completeExceptionally(AuthExceptions.TooManyRequestsException(title = context.getString(R.string.tooManyFailedAttempts), message = context.getString(R.string.manyFailedAttempts)))//show dialog
                            return@addOnCompleteListener
                        }

                        this.completeExceptionally(exception)
                    }
                    this.complete(signInWithEmailAndPassword.isSuccessful)
                }
            this.await()
        }
    }


    override suspend fun signInWithCredentials(v: AuthCredential): Boolean {
        return CompletableDeferred<Boolean>().run {
            FirebaseAuth.getInstance().signInWithCredential(v).addOnCompleteListener { signInWithCredential ->
                signInWithCredential.exception?.let { exception ->
                    if (exception.message == FB_CONNECTION_FAILURE) {
                        this.completeExceptionally(AuthExceptions.InternetException(message = context.getString(R.string.no_internet_message)))
                        return@addOnCompleteListener
                    }
                    this.completeExceptionally(AuthExceptions.ConflictFBTokenException("// todo Remove existing fb token to refresh"))

                }
                this.complete(signInWithCredential.isSuccessful)
            }
            this.await()
        }
    }
    override fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    override fun getId(): String? {
       return FirebaseAuth.getInstance().currentUser?.uid
    }

    override  fun getEmail(): String? {
        return FirebaseAuth.getInstance().currentUser?.email
    }

    override  fun getName(): String? {
        return FirebaseAuth.getInstance().currentUser?.displayName
    }

    override fun getPhoneNumber(): String?{
        return FirebaseAuth.getInstance().currentUser?.phoneNumber
    }

    override fun getPhotoUrl(): Uri? {
        return FirebaseAuth.getInstance().currentUser?.photoUrl
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


}