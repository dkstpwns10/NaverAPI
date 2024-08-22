package com.chzzk.temtem.api


import com.chzzk.temtem.domain.StreamDetail
import com.chzzk.temtem.domain.StreamSimple
import com.chzzk.temtem.domain.StreamStatus
import com.chzzk.temtem.domain.User
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.http2.Http2Reader.Companion.logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
//https://api.chzzk.naver.com/polling/v2/channels/
private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.chzzk.naver.com/polling/v2/channels/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val streamService = retrofit.create(ApiService::class.java)

val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .retryOnConnectionFailure(true)
    .build()


interface ApiService{
    @GET("/a7e175625fdea5a7d98428302b7aa57f")
    suspend fun getSimpleStream():StreamSimple

    @GET("a7e175625fdea5a7d98428302b7aa57f/live-status")
//    @GET("streaming")
    suspend fun getDetailStatus():StreamStatus

    @GET("api_call")
    suspend fun getDetailStream():StreamDetail


}

object RetrofitClient {
    private const val BASE_URL = "https://api.chzzk.naver.com/polling/v2/channels/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}