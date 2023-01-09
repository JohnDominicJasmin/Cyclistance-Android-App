package cyclistance.repositories
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAuthRepository(
    private var reloadEmail: Boolean = false,
    private var signOut: Boolean = false,
    private var sendEmailVerification: Boolean = false,
    private var email: String? = "",
    private var password: String? = "",
    private var name: String? = "",
    private var phoneNumber: String? = "",
    private var id: String? = "",
    private var photoUrl: String? = "",
    private var isSignedInWithProvider: Boolean? = false,
    private var isEmailVerified: Boolean? = false,
    private var hasAccountSignedIn: Boolean = false,
    private var imagePath: String = "",
    private var shouldReturnNetworkError: Boolean = false,
    private var signInCredentials: String = ""): AuthRepository<String> {




    fun shouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value
    }
    
    fun setReloadedEmail(value: Boolean){
        reloadEmail = value
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

    override suspend fun signInWithCredentials(v: String): Boolean {
        
        if(shouldReturnNetworkError){
            throw AuthExceptions.NetworkException("Network error")
        }
        signInCredentials = v
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