package com.chzzk.temtem.api


import com.chzzk.temtem.domain.StreamDetail
import com.chzzk.temtem.domain.StreamSimple
import com.chzzk.temtem.domain.User
import okhttp3.OkHttpClient
import okhttp3.internal.http2.Http2Reader.Companion.logger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api.chzzk.naver.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val streamService = retrofit.create(ApiService::class.java)

interface ApiService{
    @GET("a7e175625fdea5a7d98428302b7aa57f")
    suspend fun getSimpleStream():StreamSimple

    @GET("service/v1/channels/a7e175625fdea5a7d98428302b7aa57f/live-detail")
    suspend fun getDetailStream():StreamDetail

    @GET("login/naver")
    fun loginWithNaver(): Call<User>?


}
class RetrofitClient {
    companion object {
        private const val BASE_URL = "https://api.chzzk.naver.com/"
        fun create(): ApiService {
            val client = OkHttpClient.Builder().build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}