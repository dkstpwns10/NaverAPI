package com.chzzk.temtem.domain

import com.chzzk.temtem.api.ApiService
import com.chzzk.temtem.api.RetrofitClient
import com.chzzk.temtem.service.MainViewModel
import retrofit2.Call
import retrofit2.Callback

data class StreamDetail(
    val code : Int,
    val message : String,
    val content : DetailContent

) {
    suspend fun enqueue(callback: Callback<StreamDetail>) {
        val call: StreamDetail = RetrofitClient.create().getDetailStream()
        call.enqueue(callback)
    }
}

data class StreamSimple(
    val code : Int,
    val message : String?,
    val content : SimpleContent
)