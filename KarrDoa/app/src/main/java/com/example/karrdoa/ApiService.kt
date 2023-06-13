package com.example.karrdoa

import android.app.usage.UsageEvents
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/api/User/login")
    @JvmSuppressWildcards
    fun loginRequest(@Body body: Map<String, Any>): Call<ResponseBody>

    @POST("/api/User/register")
    @JvmSuppressWildcards
    fun registerRequest(@Body body: Map<String, Any>): Call<ResponseBody>

    @GET("/api/Event/get-event-all")
    @JvmSuppressWildcards
    fun getEvents(@Header("Authorization") token: String): Call<ResponseBody>
}