package com.chzzk.temtem.service

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chzzk.temtem.api.ApiService
import com.chzzk.temtem.api.RetrofitClient
import com.chzzk.temtem.api.streamService
import com.chzzk.temtem.domain.DetailContent
import com.chzzk.temtem.domain.StreamDetail
import com.chzzk.temtem.domain.StreamSimple
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class MainViewModel : ViewModel() {
    private val _streamDetailState = mutableStateOf(StreamDetailState())
    val streamDetailState: State<StreamDetailState> = _streamDetailState

    private val _streamSimpleState = mutableStateOf(StreamSimpleState())
    val streamSimpleState: State<StreamSimpleState> = _streamSimpleState

    fun getStreamData(scope: CoroutineScope) {
        Log.d("MainViewModel", "getStreamData() called")
        scope.launch {
            Log.d("MainViewModel", "Coroutine scope launched")

            print("실행됨")
        }
    }

//    private suspend fun fetchLiveDetail() {
//        Log.d("fetchLiveDetail", "fetchLiveDetail launched")
//        try {
//            Log.d("fetchLiveDetail", "fetchLiveDetail try")
//            val apiService = RetrofitClient.create()
//            val response = apiService.getDetailStream()
//            response.enqueue(object : Callback<StreamDetail> {
//                override fun onResponse(call: Call<StreamDetail>, response: Response<StreamDetail>) {
//                    if (response.isSuccessful) {
//                        Log.d("fetchLiveDetail", "try success")
//                        val streamDetail = response.body()
//                        val stream = streamDetail?.content
//                        stream?.let {
//                            val streamDetail = DetailContent(
//                                liveId = stream.liveId,
//                                liveTitle = stream.liveTitle,
//                                status = stream.status,
//                                liveImageUrl = stream.liveImageUrl,
//                                defaultThumbnailImageUrl = stream.defaultThumbnailImageUrl,
//                                concurrentUserCount = stream.concurrentUserCount,
//                                accumulateCount = stream.accumulateCount,
//                                openDate = stream.openDate,
//                                closeDate = stream.closeDate,
//                                adult = stream.adult,
//                                clipActive = stream.clipActive,
//                                tags = stream.tags,
//                                categoryType = stream.categoryType,
//                                liveCategory = stream.liveCategory,
//                                liveCategoryValue = stream.liveCategoryValue,
//                                chatActive = stream.chatActive,
//                                chatAvailableGroup = stream.chatAvailableGroup,
//                                paidPromotion = stream.paidPromotion,
//                                chatAvailableCondition = stream.chatAvailableCondition,
//                                minFollowerMinute = stream.minFollowerMinute,
//                                livePlaybackJson = stream.livePlaybackJson,
//                                p2pQuality = stream.p2pQuality,
//                                channel = stream.channel,
//                                chatDonationRankingExposure = stream.chatDonationRankingExposure
//                            )
//
//                            _streamDetailState.value = _streamDetailState.value.copy(
//                                data = streamDetail,
//                                loading = false,
//                                error = null,
//                                errorMsg = response.message()
//                            )
//
//                            Log.d("로그", "liveId: ${streamDetail.liveId}")
//                            Log.d("로그", "liveTitle: ${streamDetail.liveTitle}")
//                            Log.d("로그", "status: ${streamDetail.status}")
//                            Log.d("로그", "liveImageUrl: ${streamDetail.liveImageUrl}")
//                            Log.d("로그", "defaultThumbnailImageUrl: ${streamDetail.defaultThumbnailImageUrl}")
//                            Log.d("로그", "concurrentUserCount: ${streamDetail.concurrentUserCount}")
//                            Log.d("로그", "accumulateCount: ${streamDetail.accumulateCount}")
//                            Log.d("로그", "openDate: ${streamDetail.openDate}")
//                            Log.d("로그", "closeDate: ${streamDetail.closeDate}")
//                            Log.d("로그", "adult: ${streamDetail.adult}")
//                            Log.d("로그", "clipActive: ${streamDetail.clipActive}")
//                            Log.d("로그", "tags: ${streamDetail.tags}")
//                            Log.d("로그", "categoryType: ${streamDetail.categoryType}")
//                            Log.d("로그", "liveCategory: ${streamDetail.liveCategory}")
//                            Log.d("로그", "liveCategoryValue: ${streamDetail.liveCategoryValue}")
//                            Log.d("로그", "chatActive: ${streamDetail.chatActive}")
//                            Log.d("로그", "chatAvailableGroup: ${streamDetail.chatAvailableGroup}")
//                            Log.d("로그", "paidPromotion: ${streamDetail.paidPromotion}")
//                            Log.d("로그", "chatAvailableCondition: ${streamDetail.chatAvailableCondition}")
//                            Log.d("로그", "minFollowerMinute: ${streamDetail.minFollowerMinute}")
//                            Log.d("로그", "livePlaybackJson: ${streamDetail.livePlaybackJson}")
//                            Log.d("로그", "p2pQuality: ${streamDetail.p2pQuality}")
//                            Log.d("로그", "channel: ${streamDetail.channel}")
//                            Log.d("로그", "chatDonationRankingExposure: ${streamDetail.chatDonationRankingExposure}")
//                        }
//                    } else {
//                        Log.e("MainViewModel", "Response unsuccessful: ${response.message()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<StreamDetail>, t: Throwable) {
//                    Log.e("MainViewModel", "Failed to fetch data: ${t.message}")
//                }
//            })
//        } catch (e: Exception) {
//            // 네트워크 오류 또는 예외 발생 시 에러 처리
//            _streamDetailState.value = _streamDetailState.value.copy(
//                loading = false,
//                error = "Fetching Error: ${e.message}"
//            )
//            Log.e("MainViewModel", "Exception: ${e.message}")
//        }
//    }
//private suspend fun getStreamDetail() {
//    streamService.getDetailStream().enqueue(object : Callback<StreamDetail> {
//        override fun onResponse(call: Call<StreamDetail>, response: Response<StreamDetail>) {
//            if (response.isSuccessful) {
//                val streamDetail = response.body()
//                if (streamDetail != null) {
//                    val stream = streamDetail.content
//                    _streamDetailState.value = _streamDetailState.copy()
//                    Log.d("MainViewModel", "Stream detail fetched: $streamDetail")
//                } else {
//                    Log.e("MainViewModel", "Stream detail response body is null")
//                }
//            } else {
//                Log.e("MainViewModel", "Failed to fetch stream detail: ${response.errorBody()?.string()}")
//            }
//        }
//
//        override fun onFailure(call: Call<StreamDetail>, t: Throwable) {
//            Log.e("MainViewModel", "Error fetching stream detail: ${t.message}", t)
//        }
//    })
//}

    fun getStreamDetail(){
        viewModelScope.launch {

                val response = RetrofitClient.create().getDetailStream()
                _streamDetailState.value = _streamDetailState.value.copy(
                    data = response.content,
                    loading = false,
                    error = null
                )
                Log.d("getStreamDetailw", "getStreamDetail called")

        }
    }
    private fun fetchStreamSimple() {
        viewModelScope.launch {
            try {
                val response = streamService.getSimpleStream()
                _streamSimpleState.value = _streamSimpleState.value.copy(
                    data = response,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _streamSimpleState.value = streamSimpleState.value.copy(
                    loading = false,
                    error = "Fetching Error : ${e.message}"
                )
            }
        }
    }

    data class StreamDetailState(
        val loading: Boolean = true,
        val data: DetailContent? = null,
        val error: String? = null,
        val errorMsg: String? = null
    )

    data class StreamSimpleState(
        val loading: Boolean = true,
        val data: StreamSimple? = null,
        val error: String? = null
    )
}
