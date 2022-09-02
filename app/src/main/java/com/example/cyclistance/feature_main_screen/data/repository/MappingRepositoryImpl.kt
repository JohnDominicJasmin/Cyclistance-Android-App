package com.example.cyclistance.feature_main_screen.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.cyclistance.core.utils.MappingConstants.BIKE_TYPE_KEY
import com.example.cyclistance.feature_main_screen.data.CyclistanceApi
import com.example.cyclistance.feature_main_screen.data.mapper.UserMapper.toUser
import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_main_screen.domain.model.*
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository
import com.example.cyclistance.core.utils.SharedLocationManager
import com.example.cyclistance.core.utils.SharedLocationModel
import com.example.cyclistance.core.utils.dataStore
import com.example.cyclistance.feature_main_screen.data.remote.dto.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


class MappingRepositoryImpl(
    private val sharedLocationManager: SharedLocationManager,
    private val api: CyclistanceApi,
    context: Context) : MappingRepository {

    private var dataStore = context.dataStore


    override fun getPreference(): Flow<String?> {
        return dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                Timber.e(message = exception.localizedMessage ?: "Unexpected error occurred.")
            }
        }.map { preference ->
            preference[BIKE_TYPE_KEY]
        }
    }

    override suspend fun updatePreference(value: String?) {
        dataStore.edit { preferences ->
            preferences[BIKE_TYPE_KEY] = value!!
        }
    }

    override fun getUserLocation(): Flow<SharedLocationModel> {
        return sharedLocationManager.locationFlow().distinctUntilChanged()
    }

    override suspend fun getUserById(userId: String): User =
        handleException {
            api.getUserById(userId).toUser()
        }


    override suspend fun getUsers(): List<User> =
        handleException {
            api.getUsers().map { it.toUser() }
        }

    override suspend fun createUser(user: User) =
        handleException {
            with(user) {
                api.createUser(
                    userDto = UserDto(
                        address = this.address ?: return@handleException,
                        id = this.id ?: return@handleException,
                        location = this.location ?: return@handleException,
                        name = this.name ?: return@handleException,
                        userNeededHelp = this.userNeededHelp,
                        userAssistance = this.userAssistance,
                        profilePictureUrl = this.profilePictureUrl ?: return@handleException,
                        contactNumber = this.contactNumber ?: return@handleException,
                        rescueRequest = this.rescueRequest
                    ))
            }
        }

    override suspend fun updateUser(itemId: String, user: User) =
        handleException {
            with(user) {
                api.updateUser(
                    itemId = itemId,
                    userDto = UserDto(
                        address = this.address,
                        id = this.id,
                        location = this.location,
                        name = this.name,
                        userNeededHelp = this.userNeededHelp,
                        userAssistance = this.userAssistance,
                        profilePictureUrl = this.profilePictureUrl,
                        contactNumber = this.contactNumber
                    ))

            }
        }

    override suspend fun deleteUser(id: String) =
        handleException {
            api.deleteUser(id)
        }


}

private suspend fun <T> handleException(action: suspend () -> T): T {
    return try {
        action()
    } catch (e: HttpException) {
        throw MappingExceptions.UnexpectedErrorException()
    } catch (e: IOException) {
        throw MappingExceptions.NoInternetException()
    }
}