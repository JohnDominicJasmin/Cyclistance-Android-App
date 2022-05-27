package com.example.cyclistance.feature_main_screen.data.repository

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
import retrofit2.HttpException
import java.io.IOException

class MappingRepositoryImpl(private val api: CyclistanceApi): MappingRepository {

    override suspend fun getUserById(userId: String): User {
       return try{
            api.getUserById(userId).toUser()
       }catch (e:HttpException){
           throw MappingExceptions.UnexpectedErrorException(e.message ?: "Unexpected error occurred.")
       }catch (e:IOException){
           throw MappingExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
       }
    }

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
                        address = this.address,
                        id = this.id,
                        location = this.location,
                        name = this.name,
                        userNeededHelp = this.userNeededHelp,
                        userAssistance = this.userAssistance
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
                      address = this.address,
                      id = this.id,
                      location = this.location,
                      name = this.name,
                      userNeededHelp = this.userNeededHelp,
                      userAssistance = this.userAssistance
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