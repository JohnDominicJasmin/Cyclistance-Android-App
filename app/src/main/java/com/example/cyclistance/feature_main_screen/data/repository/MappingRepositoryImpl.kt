package com.example.cyclistance.feature_main_screen.data.repository

import com.example.cyclistance.feature_main_screen.data.CyclistanceApi
import com.example.cyclistance.feature_main_screen.data.mapper.UserMapper.toCancellationEvent
import com.example.cyclistance.feature_main_screen.data.mapper.UserMapper.toHelpRequest
import com.example.cyclistance.feature_main_screen.data.mapper.UserMapper.toUser
import com.example.cyclistance.feature_main_screen.data.mapper.UserMapper.toUserAssistance
import com.example.cyclistance.feature_main_screen.data.remote.dto.CancellationEventDto
import com.example.cyclistance.feature_main_screen.data.remote.dto.HelpRequestDto
import com.example.cyclistance.feature_main_screen.data.remote.dto.UserAssistanceDto
import com.example.cyclistance.feature_main_screen.data.remote.dto.UserDto
import com.example.cyclistance.feature_main_screen.domain.exceptions.CustomExceptions
import com.example.cyclistance.feature_main_screen.domain.model.*
import com.example.cyclistance.feature_main_screen.domain.repository.MappingRepository
import retrofit2.HttpException
import java.io.IOException

class MappingRepositoryImpl(private val api: CyclistanceApi): MappingRepository {

    override suspend fun getUserById(userId: String): User {
       return try{
            api.getUserById(userId).toUser()
       }catch (e:HttpException){
           throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
       }catch (e:IOException){
           throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
       }
    }

    override suspend fun getUsers(): List<User> {
        return try{
            api.getUsers().map { it.toUser() }
        }catch (e:HttpException){
            throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
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
                        name = this.name
                    ))
            }
        }catch (e:HttpException){
            throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun getUserAssistanceById(userId: String): UserAssistance {
        return try{
            api.getUserAssistanceById(userId).toUserAssistance()
        }catch (e:HttpException){
            throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun getUsersAssistance(): List<UserAssistance> {
        return try{
            api.getUsersAssistance().map{ it.toUserAssistance() }
        }catch (e:HttpException){
            throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }


    override suspend fun createUserAssistance(userAssistance: UserAssistance) {
        return try{
            with(userAssistance) {
               api.createUserAssistance(userAssistanceDto = UserAssistanceDto(
                   confirmationDetails = this.confirmationDetails,
                   id = this.id,
                   rescueRequest = this.rescueRequest,
                   status = this.status
               ))
            }
        }catch (e:HttpException){
            throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun getHelpRequestById(userId: String, clientId: String): HelpRequest {
        return try{
            api.getHelpRequestById(userId, clientId).toHelpRequest()
        }catch (e:HttpException){
            throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun createHelpRequest(helpRequest: HelpRequest) {
        return try{
            with(helpRequest) {
                api.createHelpRequest(helpRequestDto = HelpRequestDto(
                    accepted = this.accepted,
                    clientId = this.clientId,
                    id = this.id
                ))
            }
        }catch (e:HttpException){
            throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }

    override suspend fun getCancellationById(userId: String, clientId: String): CancellationEvent {
        return try{
            api.getCancellationById(userId, clientId).toCancellationEvent()
        }catch (e:HttpException){
            throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
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
            throw CustomExceptions.UnexpectedErrorException(e.localizedMessage ?: "Unexpected error occurred.")
        }catch (e:IOException){
            throw CustomExceptions.NoInternetException("Couldn't reach server. Check your internet connection")
        }
    }
}