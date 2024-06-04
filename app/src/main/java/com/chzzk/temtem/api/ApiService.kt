package com.chzzk.temtem.api

import com.chzzk.temtem.domain.StreamDetail
import com.chzzk.temtem.domain.StreamSimple
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder().baseUrl("https://api.chzzk.naver.com/service/v1/channels/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val streamService = retrofit.create(ApiService::class.java)

interface ApiService{
    @GET("a7e175625fdea5a7d98428302b7aa57f")
    suspend fun getSimpleStream():StreamSimple

    @GET("a7e175625fdea5a7d98428302b7aa57f/live-detail")
    suspend fun getDetailStream():StreamDetail
}