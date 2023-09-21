package com.example.cyclistance.feature_mapping.data

import com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.RescueTransactionItemDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserItemDto
import retrofit2.http.*

interface CyclistanceApi {


    @GET("/api/v1/get-user-by-id/{user_id}")
    suspend fun getUserById(@Path("user_id") userId: String): UserItemDto

    @GET("/api/v1/get-users/{latitude}&{longitude}")
    suspend fun getUsers(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double,
    ): UserDto

    @POST("/api/v1/create-user")
    suspend fun createUser(@Body userItemDto: UserItemDto)

    @DELETE("/api/v1/delete-user/{user_id}")
    suspend fun deleteUser(@Path("user_id") itemId: String)





    @DELETE("/api/v1/delete-rescue-respondent/{user_id}/{respondent_id}")
    suspend fun deleteRescueRespondent(@Path("user_id") userId: String, @Path("respondent_id") respondentId: String)

    @PUT("/api/v1/add-rescue-respondent/{user_id}/{respondent_id}")
    suspend fun addRescueRespondent(@Path("user_id") userId: String, @Path("respondent_id") respondentId: String)

    @DELETE("/api/v1/delete-all-respondents/{user_id}")
    suspend fun deleteAllRespondents(@Path("user_id") userId: String)

    @PUT("/api/v1/cancel-help-respond/{user_id}/{respondent_id}")
    suspend fun cancelHelpRespond(@Path("user_id") userId: String, @Path("respondent_id") respondentId: String)


    @GET("/api/v1/get-rescue-transaction-by-id/{rescue_transaction_id}")
    suspend fun getRescueTransactionById(@Path("rescue_transaction_id") userId: String): RescueTransactionItemDto

    @POST("/api/v1/create-rescue-transaction")
    suspend fun createRescueTransaction(@Body rescueTransactionItemDto: RescueTransactionItemDto)

    @DELETE("/api/v1/delete-rescue-transaction/{rescue_transaction_id}")
    suspend fun deleteRescueTransaction(@Path("rescue_transaction_id") itemId: String)


}






















