package com.example.cyclistance.feature_authentication.data.repositories
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAuthRepository(
    private var reloadEmail: Boolean = false,
    private var signOut: Boolean = false,
    private var sendEmailVerification: Boolean = false,
    private var email: String? = "johndoe@gmail.com",
    private var password: String? = "12341234",
    private var name: String? = "John Doe",
    private var phoneNumber: String? = "09263208902",
    private var id: String? = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN",
    private var photoUrl: String? = "http://sample_photo_url.png",
    private var isSignedInWithProvider: Boolean? = false,
    private var isEmailVerified: Boolean? = false,
    private var hasAccountSignedIn: Boolean = false,
    private var imagePath: String = "",
    private var shouldReturnNetworkError: Boolean = false,
    private var signInCredential: SignInCredential? = null): AuthRepository {


    fun signInCredential(value: SignInCredential?){
        signInCredential = value
    }

    fun reloadEmail(value: Boolean){
        reloadEmail = value
    }

    fun signOut(value: Boolean){
        signOut = value
    }

    fun sendEmailVerification(value: Boolean){
        sendEmailVerification = value
    }

    fun setEmail(value: String?){
        email = value
    }

    fun setPassword(value: String?){
        password = value
    }

    fun setName(value: String?){
        name = value
    }

    fun setPhoneNumber(value: String?){
        phoneNumber = value
    }

    fun setId(value: String?){
        id = value
    }

    fun setPhotoUrl(value: String?){
        photoUrl = value
    }

    fun isSignedInWithProvider(value: Boolean?){
        isSignedInWithProvider = value
    }

    fun isEmailVerified(value: Boolean?){
        isEmailVerified = value
    }

    fun hasAccountSignedIn(value: Boolean){
        hasAccountSignedIn = value
    }


    fun shouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value
    }

    fun setReloadedEmail(value: Boolean){
        reloadEmail = value
    }

    fun setImagePath(value: String){
        imagePath = value
    }

    override suspend fun reloadEmail(): Boolean = reloadEmail

    override fun signOut() {
        signOut = true
    }

    override fun getEmail(): String? = email

    override suspend fun sendEmailVerification(): Boolean = sendEmailVerification

    override fun getName(): String? = name

    override fun getId(): String? = id

    override fun getPhotoUrl(): String? = photoUrl

    override fun isSignedInWithProvider(): Boolean? = isSignedInWithProvider

    override fun isEmailVerified(): Boolean? = isEmailVerified

    override fun hasAccountSignedIn(): Boolean = hasAccountSignedIn

    override suspend fun uploadImage(v: String): String {

        if(shouldReturnNetworkError){
            throw AuthExceptions.NetworkException("Network error")
        }
        imagePath = v
        return imagePath
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean {
       
        if(shouldReturnNetworkError){
            throw AuthExceptions.NetworkException("Network error")
        }
        this.email = email
        this.password = password
        return true
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {
        if (shouldReturnNetworkError) {
            throw AuthExceptions.NetworkException("Network error")
        }
        
        if(email != this.email){
            throw MappingExceptions.UserException("Email is not correct")
        }
        
        if(this.password != password){
            throw MappingExceptions.UserException("Password is not correct")
        }
        
        return true
        
    }

    override suspend fun signInWithCredential(v: SignInCredential): Boolean {
        
        if(shouldReturnNetworkError){
            throw AuthExceptions.NetworkException("Network error")
        }
        signInCredential = v
        return true
    }

    override suspend fun updateProfile(v: String?, name: String?): Boolean {
        if(shouldReturnNetworkError){
            throw AuthExceptions.NetworkException("Network error")
        }
        this.name = name
        this.photoUrl = v
        return true
    }

    override suspend fun updatePhoneNumber(phoneNumber: String) {
        if(shouldReturnNetworkError){
            throw AuthExceptions.NetworkException("Network error")
        }
        this.phoneNumber = phoneNumber
    }

    override fun getPhoneNumber(): Flow<String> {
        return flow{
            phoneNumber?.let { emit(it) }
        }
    }
}