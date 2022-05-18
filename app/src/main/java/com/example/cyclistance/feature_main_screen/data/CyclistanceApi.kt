package com.example.cyclistance.feature_main_screen.data

import com.example.cyclistance.feature_main_screen.data.remote.dto.*
import retrofit2.http.GET
import retrofit2.http.Path

interface CyclistanceApi {



    @GET("/api/v1/get-user-by-id/{user_id}")
    suspend fun getUserById(@Path("user_id") user_id: String): UserDto

    @GET("/api/v1/get-users")
    suspend fun getUsers():List<UserDto>





    @GET("/api/v1/get-user-assistance-by-id/{user_id}")
    suspend fun getUserAssistanceById(@Path("user_id") user_id: String):UserAssistanceDto

    @GET("/api/v1/get-users-assistance")
    suspend fun getUsersAssistance():List<UserAssistanceDto>



    @GET("/api/v1/get-help-request-by-id/{id},{client_id}")
    suspend fun getHelpRequestById(
        @Path("id") id: String,
        @Path("client_id") clientId: String
    ): HelpRequestDto



    @GET("/api/v1/get-cancellation-event/{id},{client_id}")
    suspend fun getCancellationById(
        @Path("id") id: String,
        @Path("client_id") clientId: String
    ):CancellationEventDto

}






















