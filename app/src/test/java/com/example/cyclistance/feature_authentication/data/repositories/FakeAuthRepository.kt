package com.example.cyclistance.feature_authentication.data.repositories
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAuthRepository: AuthRepository {


    companion object {

        var reloadEmail = false
        var signOut = false
        var sendEmailVerification = false
        var email = "johndoe@gmail.com"
        var password = "12341234"
        var name:String? = "miko"
        var phoneNumber:String? = "09263208902"
        var id = "rwLt7Y9Me7JCNJ3Fhh1SP4PaizqN"
        var photoUrl:String? = "http://sample_photo_url.png"
        var isSignedInWithProvider = false
        var isEmailVerified = false
        var hasAccountSignedIn = false
        var imagePath = ""
        var shouldReturnNetworkError = false
        var signInCredential: SignInCredential? = null

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
        Companion.email = email
        Companion.password = password
        return true
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean {
        if (shouldReturnNetworkError) {
            throw AuthExceptions.NetworkException("Network error")
        }
        
        if(email != Companion.email){
            throw MappingExceptions.UserException("Email is not correct")
        }
        
        if(Companion.password != password){
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
        Companion.name = name
        photoUrl = v
        return true
    }

    override suspend fun updatePhoneNumber(phoneNumber: String) {
        if(shouldReturnNetworkError){
            throw AuthExceptions.NetworkException("Network error")
        }
        Companion.phoneNumber = phoneNumber
    }

    override fun getPhoneNumber(): Flow<String> {
        return flow{
            phoneNumber?.let { emit(it) }
        }
    }
}