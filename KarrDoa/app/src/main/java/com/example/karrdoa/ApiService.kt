package com.example.karrdoa

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("/api/User/login")
    @JvmSuppressWildcards
    fun loginRequest(@Body body: Map<String, Any>): Call<ResponseBody>

    @POST("/api/User/register")
    @JvmSuppressWildcards
    fun registerRequest(@Body body: Map<String, Any>): Call<ResponseBody>

    @GET("/api/Event/get-event-all")
    @JvmSuppressWildcards
    fun getEvents(@Header("Authorization") token: String): Call<List<EventsItem>>

    @GET("/api/Event/get-event-by-user-id")
    @JvmSuppressWildcards
    fun getEventsbyId(@Header("Authorization") token: String): Call<List<EventsItem>>

    @POST("/api/Event/create-event")
    @JvmSuppressWildcards
    fun createEvent(@Body body: Map<String, Any>, @Header("Authorization") token: String): Call<ResponseBody>

    @POST("/api/Event/user-event-join")
    @JvmSuppressWildcards
    fun eventJoin(@Body body: Map<String, Any>, @Header("Authorization") token: String): Call<List<String>>

    @GET("/api/User")
    @JvmSuppressWildcards
    fun getUser(@Header("Authorization") token: String): Call<ProfileResponse>

}