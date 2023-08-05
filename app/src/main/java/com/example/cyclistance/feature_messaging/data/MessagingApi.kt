package com.example.cyclistance.feature_messaging.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST



interface MessagingApi {
    @POST("send")
    fun sendMessage(
        @HeaderMap headers: HashMap<String, String>,
        @Body message: String
    ): Call<String>
}