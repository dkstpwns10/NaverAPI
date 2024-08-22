package com.chzzk.temtem.api

import com.chzzk.temtem.domain.AlarmData
import com.chzzk.temtem.domain.NoticeData
import com.chzzk.temtem.domain.WiterData
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface DBApi {
    @GET("/alarm_data")
    suspend fun getAlarmData(@Query("page_number") pageNumber: Int): List<AlarmData>

    @GET("/witer_data")
    suspend fun getWiterData(@Query("page_number") pageNumber: Int): List<WiterData>

    @GET("/notice_data")
    suspend fun getNoticeData(@Query("page_number") pageNumber: Int): List<NoticeData>
}

object RetrofitDB {
    private const val BASE_URL = "http://172.30.1.42:5000"

    val api: DBApi by lazy {
        val gson = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(DBApi::class.java)
    }
}