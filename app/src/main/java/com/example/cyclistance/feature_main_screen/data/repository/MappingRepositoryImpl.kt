package com.example.cyclistance.feature_main_screen.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.cyclistance.core.utils.MappingConstants.BIKE_TYPE_KEY
import com.example.cyclistance.feature_main_screen.data.CyclistanceApi
import com.example.cyclistance.feature_main_screen.data.mapper.UserMapper.toCancellationEvent
import com.example.cyclistance.feature_main_screen.data.mapper.UserMapper.toRescueRequest
import com.example.cyclistance.feature_main_screen.data.mapper.UserMapper.toUser
import com.example.cyclistance.feature_main_screen.data.remote.dto.CancellationEventDto
import com.example.cyclistance.feature_main_screen.data.remote.dto.RescueRequestDto
import com.example.cyclistance.feature_main_screen.data.remote.dto.UserDto
import com.example.cyclistance.feature_main_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_main_screen.domain.model.*
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository
import com.example.cyclistance.core.utils.SharedLocationManager
import com.example.cyclistance.core.utils.SharedLocationModel
import com.example.cyclistance.core.utils.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException


class MappingRepositoryImpl(
    private val sharedLocationManager: SharedLocationManager,
    private val api: CyclistanceApi,
    context: Context): MappingRepository {

    private var dataStore = context.dataStore


    override fun getPreference(): Flow<String?> {
        return dataStore.data.catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                Timber.e(message = exception.localizedMessage ?: "Unexpected error occurred.")
            }
        }.map { preference ->
            preference[BIKE_TYPE_KEY]
        }
    }

    override suspend fun updatePreference(value: String?) {
        dataStore.edit{preferences ->
            preferences[BIKE_TYPE_KEY] = value!!
        }
    }

    override fun getUserLocation(): Flow<SharedLocationModel> {
        return sharedLocationManager.locationFlow()
    }

    override suspend fun getUserById(userId: String): User {
       return try{
            api.getUserById(userId).toUser()
       }catch (e:HttpException){
           throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
       }catch (e:IOException){
           throw MappingExceptions.NoInternetException()
       }
    }

    //todo: add cold flows here
    override suspend fun getUsers(): List<User> {
        return try{
            api.getUsers().map { it.toUser() }
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun createUser(user: User) {
        return try{
            with(user) {
                api.createUser(
                    userDto = UserDto(
                        address = this.address?:return,
                        id = this.id?:return,
                        location = this.location?:return,
                        name = this.name?:return,
                        userNeededHelp = this.userNeededHelp,
                        userAssistance = this.userAssistance,
                        profilePictureUrl = this.profilePictureUrl?:return,
                        contactNumber = this.contactNumber?:return

                    ))
            }
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun updateUser(itemId:String, user: User) {
        return try{
            with(user) {
              api.updateUser(
                  itemId = itemId,
                  userDto = UserDto(
                      address = this.address?:return,
                      id = this.id?:return,
                      location = this.location?:return,
                      name = this.name?:return,
                      userNeededHelp = this.userNeededHelp,
                      userAssistance = this.userAssistance,
                      profilePictureUrl = this.profilePictureUrl?:return,
                      contactNumber = this.contactNumber?:return
                  )
              )
            }
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun deleteUser(id: String) {
        return try{
            api.deleteUser(id)
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }








    override suspend fun getRescueRequest(eventId: String): RescueRequest {
        return try{
            api.getRescueRequest(eventId).toRescueRequest()
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun createRescueRequest(rescueRequest: RescueRequest) {
        return try{
            with(rescueRequest) {
                api.createRescueRequest(
                    rescueRequestDto = RescueRequestDto(
                        rescueEventId = this.rescueEventId,
                        respondents = this.respondents
                    )
                )
            }
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun updateRescueRequest(eventId: String, rescueRequest: RescueRequest) {
        return try{
            with(rescueRequest) {
                api.updateRescueRequest(
                    eventId = eventId,
                    rescueRequestDto = RescueRequestDto(
                        rescueEventId = this.rescueEventId,
                        respondents = this.respondents
                    )
                )
            }
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun deleteRescueRequest(id: String) {
        return try{
            api.deleteRescueRequest(id)
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun getCancellationById(userId: String, clientId: String): CancellationEvent {
        return try{
            api.getCancellationById(userId, clientId).toCancellationEvent()
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun createCancellationEvent(cancellationEvent: CancellationEvent) {
        return try{
            with(cancellationEvent) {
                api.createCancellationEvent(cancellationEventDto = CancellationEventDto(
                    cancellationReasons = this.cancellationReasons,
                    clientId = this.clientId,
                    id = this.id
                ))
            }
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun updateCancellationEvent(itemId: String, cancellationEvent: CancellationEvent) {
        return try{
            with(cancellationEvent) {
                api.updateCancellationEvent(
                    itemId = itemId,
                    cancellationEventDto = CancellationEventDto(
                    cancellationReasons = this.cancellationReasons,
                    clientId = this.clientId,
                    id = this.id
                ))
            }
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun deleteCancellationEvent(id: String) {
        return try{
            api.deleteCancellationEvent(id)
        }catch (e:HttpException){
            throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }
}