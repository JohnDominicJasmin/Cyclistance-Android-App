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
import com.example.cyclistance.core.utils.editData
import com.example.cyclistance.core.utils.getData
import com.example.cyclistance.core.utils.service.LocationService
import com.example.cyclistance.feature_mapping_screen.data.CyclistanceApi
import com.example.cyclistance.feature_mapping_screen.data.mapper.RescueTransactionMapper.toRescueTransaction
import com.example.cyclistance.feature_mapping_screen.data.mapper.RescueTransactionMapper.toRescueTransactionDto
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toUser
import com.example.cyclistance.feature_mapping_screen.data.mapper.UserMapper.toUserDto
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class MappingRepositoryImpl(
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

    override suspend fun getUserById(userId: String): User =
        handleException {
            api.getUserById(userId).toUser()
        }

    override fun getUserLocation(): Flow<Location> {
        return LocationService.address
    }

    override suspend fun getUsers(): List<User> =
        handleException {
            api.getUsers().map { it.toUser() }
        }

    override suspend fun createUser(user: User) =
        handleException {
                api.createUser(userDto = user.toUserDto())
        }

    override suspend fun updateUser(itemId: String, user: User) =
        handleException {
                api.updateUser(
                    itemId = itemId,
                    userDto = user.toUserDto())
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

    override suspend fun getRescueTransactionById(userId: String): RescueTransaction =
        handleException {
            api.getRescueTransactionById(userId).toRescueTransaction()
        }

    override suspend fun createRescueTransaction(rescueTransaction: RescueTransaction) =
        handleException {
            api.createRescueTransaction(rescueTransaction.toRescueTransactionDto())
        }

    override suspend fun updateRescueTransaction(
        itemId: String,
        rescueTransaction: RescueTransaction) {

        handleException {
            api.updateRescueTransaction(
                itemId = itemId,
                rescueTransactionDto = rescueTransaction.toRescueTransactionDto())
        }
    }

    override suspend fun deleteRescueTransaction(id: String) {
        handleException {
            api.deleteRescueTransaction(id)
        }
    }
}




private inline fun <T> handleException(action: () -> T): T {
    return try {
        action()
    } catch (e: HttpException) {
        throw MappingExceptions.UnexpectedErrorException(message = e.message ?: "Unexpected Error")
    } catch (e: IOException) {
        Timber.e("Exception is ${e.message}")
        throw MappingExceptions.NetworkExceptions()
    }
}