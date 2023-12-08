package com.myapp.cyclistance.feature_mapping.data

import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.RescueTransactionItemDto
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserItemDto
import retrofit2.http.*

interface CyclistanceApi {


    @GET("/api/v1/get-user-by-id/{user_id}")
    suspend fun getUserById(@Path("user_id") userId: String): UserItemDto

    @POST("/api/v1/create-user")
    suspend fun createUser(@Body userItemDto: UserItemDto)

    @DELETE("/api/v1/delete-user/{user_id}")
    suspend fun deleteUser(@Path("user_id") itemId: String)

    @PUT("/app/v1/remove-user-transaction/{id}")
    suspend fun removeUserTransaction(@Path("id") id: String)



    @DELETE("/api/v1/delete-rescue-respondent/{user_id}/{respondent_id}")
    suspend fun deleteRescueRespondent(@Path("user_id") userId: String, @Path("respondent_id") respondentId: String)

    @PUT("/api/v1/add-rescue-respondent/{user_id}/{respondent_id}")
    suspend fun addRescueRespondent(@Path("user_id") userId: String, @Path("respondent_id") respondentId: String)

    @DELETE("/api/v1/delete-all-respondents/{user_id}")
    suspend fun deleteAllRespondents(@Path("user_id") userId: String)

    @PUT("/api/v1/cancel-help-respond/{user_id}/{respondent_id}")
    suspend fun cancelHelpRespond(@Path("user_id") userId: String, @Path("respondent_id") respondentId: String)


    @PUT("api/v1/accept-rescue-request/{user_id}/{rescuer_id}")
    suspend fun acceptRescueRequest(@Path("user_id") userId: String, @Path("rescuer_id") rescuerId: String)

    @GET("/api/v1/get-rescue-transaction-by-id/{rescue_transaction_id}")
    suspend fun getRescueTransactionById(@Path("rescue_transaction_id") userId: String): RescueTransactionItemDto

    @POST("/api/v1/create-rescue-transaction")
    suspend fun createRescueTransaction(@Body rescueTransactionItemDto: RescueTransactionItemDto)

    @DELETE("/api/v1/delete-rescue-transaction/{rescue_transaction_id}")
    suspend fun deleteRescueTransaction(@Path("rescue_transaction_id") itemId: String)


}






















