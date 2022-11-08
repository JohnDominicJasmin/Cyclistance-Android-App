package com.example.cyclistance.feature_mapping_screen.data.repository

import android.content.Context
import android.graphics.drawable.Drawable
import android.location.Location
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil.imageLoader
import coil.request.ImageRequest
import com.example.cyclistance.core.utils.constants.MappingConstants.ADDRESS_KEY
import com.example.cyclistance.core.utils.constants.MappingConstants.BIKE_TYPE_KEY
import com.example.cyclistance.core.utils.extension.editData
import com.example.cyclistance.core.utils.extension.getData
import com.example.cyclistance.core.utils.service.LocationService
import com.example.cyclistance.core.utils.websockets.WebSocketClient
import com.example.cyclistance.feature_mapping_screen.data.CyclistanceApi
import com.example.cyclistance.feature_mapping_screen.data.mapper.RescueTransactionMapper.toRescueTransaction
import com.example.cyclistance.feature_mapping_screen.data.mapper.RescueTransactionMapper.toRescueTransactionDto
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toUser
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toUserItem
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toUserItemDto
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class MappingRepositoryImpl(
    val rescueTransactionClient: WebSocketClient<RescueTransaction>,
    val userClient: WebSocketClient<User>,
    val imageRequestBuilder: ImageRequest.Builder,
    private val api: CyclistanceApi,
    val context: Context) : MappingRepository {


    private var dataStore = context.dataStore


    override fun getBikeType(): Flow<String> {
        return dataStore.getData(key = BIKE_TYPE_KEY, defaultValue = "")
    }

    override suspend fun updateBikeType(bikeType: String) {
        dataStore.editData(BIKE_TYPE_KEY, bikeType)
    }

    override fun getAddress(): Flow<String> {
        return dataStore.getData(key = ADDRESS_KEY, defaultValue = "")
    }

    override suspend fun updateAddress(address: String) {
        dataStore.editData(ADDRESS_KEY, address)
    }

    override suspend fun getUserById(userId: String): UserItem =
        handleException {
            api.getUserById(userId).toUserItem()
        }

    override fun getUserLocation(): Flow<Location> {
        return LocationService.address
    }

    override suspend fun getUsers(): User =
        handleException {
            api.getUsers().toUser()
        }

    override suspend fun createUser(userItem: UserItem) =
        handleException {
                api.createUser(userItemDto = userItem.toUserItemDto())
        }

    override suspend fun deleteUser(id: String) =
        handleException {
            api.deleteUser(id)
        }

    override suspend fun imageUrlToDrawable(imageUrl: String): Drawable {
        return handleException {
            withContext(Dispatchers.IO) {
                val request = imageRequestBuilder
                    .data(imageUrl)
                    .diskCacheKey(imageUrl)
                    .memoryCacheKey(imageUrl)
                    .diskCacheKey(imageUrl)
                    .memoryCacheKey(imageUrl)
                    .build()
                val imageResult = context.imageLoader.execute(request)
                imageResult.drawable!!
            }
        }
    }

    override suspend fun getRescueTransactionById(userId: String): RescueTransactionItem =
        handleException {
            api.getRescueTransactionById(userId).toRescueTransaction()
        }

    override suspend fun createRescueTransaction(rescueTransaction: RescueTransactionItem) =
        handleException {
            api.createRescueTransaction(rescueTransaction.toRescueTransactionDto())
        }



    override suspend fun deleteRescueTransaction(id: String) {
        handleException {
            api.deleteRescueTransaction(id)
        }
    }

    override fun getUserUpdates(): Flow<User> {
        return userClient.getResult()
    }

    override fun broadCastUser() {
        userClient.broadCastEvent()
    }

    override fun broadCastRescueTransaction() {
        rescueTransactionClient.broadCastEvent()
    }

    override fun getRescueTransactionUpdates(): Flow<RescueTransaction> {
        return rescueTransactionClient.getResult()
    }

}




private inline fun <T> handleException(action: () -> T): T {
    return try {
        action()
    } catch (e: HttpException) {
        throw MappingExceptions.UnexpectedErrorException(message = e.message ?: "Unexpected Error")
    } catch (e: IOException) {
        Timber.e("Exception is ${e.message}")
        throw MappingExceptions.NetworkException()
    }
}