package com.chzzk.temtem.api

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.ui.platform.LocalContext
import com.chzzk.temtem.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

// 네이버 API 인터페이스
interface NaverApiService {
    @GET("v1/nid/me")
    fun getUserProfile(@Header("Authorization") accessToken: String): Call<NaverUserProfileResponse>

    @GET
    fun logout(
        @Url url: String,
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("access_token") accessToken: String,
        @Query("service_provider") serviceProvider: String = "NAVER"
    ): Call<Void>
}


data class NaverUserProfileResponse(
    val resultcode: String,
    val message: String,
    val response: NaverUserProfile
)

data class NaverUserProfile(
    val id: String,
    val name: String,
    val email: String
)


private var retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://openapi.naver.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val naverApiService: NaverApiService = retrofit.create(NaverApiService::class.java)
