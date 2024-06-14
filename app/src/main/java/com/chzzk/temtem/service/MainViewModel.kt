package com.chzzk.temtem.service

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chzzk.temtem.api.ApiService
import com.chzzk.temtem.api.RetrofitClient
import com.chzzk.temtem.api.streamService
import com.chzzk.temtem.domain.DetailContent
import com.chzzk.temtem.domain.StatusContent
import com.chzzk.temtem.domain.StreamDetail
import com.chzzk.temtem.domain.StreamSimple
import com.chzzk.temtem.utils.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _streamDetailState = mutableStateOf(StreamDetailState())
    val streamDetailState: State<StreamDetailState> = _streamDetailState

    private val _streamSimpleState = mutableStateOf(StreamSimpleState())
    val streamSimpleState: State<StreamSimpleState> = _streamSimpleState

    private val _streamStatusState = mutableStateOf(StreamStatusState())
    val streamStatusState: State<StreamStatusState> = _streamStatusState
//디테일
    fun fetchLiveDetail() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            if (!isNetworkAvailable(context)) {
                _streamDetailState.value = _streamDetailState.value.copy(
                    loading = false,
                    error = "Network is not available"
                )
                return@launch
            }

            Log.d("fetchLiveDetail", "fetchLiveDetail launched")
            _streamDetailState.value = _streamDetailState.value.copy(loading = true)
            try {
                Log.d("fetchLiveDetail", "fetchLiveDetail try")
                val streamDetail = RetrofitClient.api.getDetailStatus()
                val stream = streamDetail.content
                stream?.let {
                    Log.d("fetchLiveDetail", "let launched")
                    val statusContent = StatusContent(
                        liveTitle = it.liveTitle,
                        status = it.status,
                        concurrentUserCount = it.concurrentUserCount,
                        accumulateCount = it.accumulateCount,
                        paidPromotion = it.paidPromotion,
                        adult = it.adult,
                        clipActive = it.clipActive,
                        chatChannelId = it.chatChannelId,
                        tags = it.tags,
                        categoryType = it.categoryType,
                        liveCategory = it.liveCategory,
                        liveCategoryValue = it.liveCategoryValue,
                        livePollingStatusJson = it.livePollingStatusJson,
                        faultStatus = it.faultStatus,
                        userAdultStatus = it.userAdultStatus,
                        chatActive = it.chatActive,
                        chatAvailableGroup = it.chatAvailableGroup,
                        chatAvailableCondition = it.chatAvailableCondition,
                        minFollowerMinute = it.minFollowerMinute,
                        chatDonationRankingExposure = it.chatDonationRankingExposure
                    )

                    _streamStatusState.value = _streamStatusState.value.copy(
                        data = statusContent,
                        loading = false,
                        error = null
                    )

                    Log.d("로그", "liveId: ${statusContent.liveTitle}")
                    Log.d("로그", "status: ${statusContent.status}")
                }
            } catch (e: SocketTimeoutException) {
                Log.e("MainViewModel", "SocketTimeoutException: ${e.message}")
                _streamDetailState.value = _streamDetailState.value.copy(
                    loading = false,
                    error = "Timeout Error: ${e.message}"
                )
            } catch (e: Exception) {
                Log.e("MainViewModel", "Exception: ${e.message}", e)
                _streamDetailState.value = _streamDetailState.value.copy(
                    loading = false,
                    error = "Fetching Error: ${e.message}"
                )
            }
        }
    }

//간략
    fun fetchLiveStatus() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            if (!isNetworkAvailable(context)) {
                _streamDetailState.value = _streamDetailState.value.copy(
                    loading = false,
                    error = "Network is not available"
                )
                return@launch
            }

            Log.d("fetchLiveDetail", "fetchLiveDetail launched")
            _streamDetailState.value = _streamDetailState.value.copy(loading = true)
            try {
                Log.d("fetchLiveDetail", "fetchLiveDetail try")
                val streamDetail = RetrofitClient.api.getDetailStatus()
                val stream = streamDetail.content
                stream?.let {
                    Log.d("fetchLiveDetail", "let launched")
                    val statusContent = StatusContent(
                        liveTitle = it.liveTitle,
                        status = it.status,
                        concurrentUserCount = it.concurrentUserCount,
                        accumulateCount = it.accumulateCount,
                        paidPromotion = it.paidPromotion,
                        adult = it.adult,
                        clipActive = it.clipActive,
                        chatChannelId = it.chatChannelId,
                        tags = it.tags,
                        categoryType = it.categoryType,
                        liveCategory = it.liveCategory,
                        liveCategoryValue = it.liveCategoryValue,
                        livePollingStatusJson = it.livePollingStatusJson,
                        faultStatus = it.faultStatus,
                        userAdultStatus = it.userAdultStatus,
                        chatActive = it.chatActive,
                        chatAvailableGroup = it.chatAvailableGroup,
                        chatAvailableCondition = it.chatAvailableCondition,
                        minFollowerMinute = it.minFollowerMinute,
                        chatDonationRankingExposure = it.chatDonationRankingExposure
                    )

                    _streamStatusState.value = _streamStatusState.value.copy(
                        data = statusContent,
                        loading = false,
                        error = null
                    )

                    Log.d("로그", "liveId: ${statusContent.liveTitle}")
                    Log.d("로그", "status: ${statusContent.status}")
                }
            } catch (e: SocketTimeoutException) {
                Log.e("MainViewModel", "SocketTimeoutException: ${e.message}")
                _streamStatusState.value = _streamStatusState.value.copy(
                    loading = false,
                    error = "Timeout Error: ${e.message}"
                )
            } catch (e: Exception) {
                Log.e("MainViewModel", "Exception: ${e.message}", e)
                _streamStatusState.value = _streamStatusState.value.copy(
                    loading = false,
                    error = "Fetching Error: ${e.message}"
                )
            }
        }
    }


    private fun fetchStreamSimple() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getSimpleStream()
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

    data class StreamStatusState(
        val loading: Boolean = true,
        val data: StatusContent? = null,
        val error: String? = null,
        val errorMsg: String? = null
    )

    data class StreamSimpleState(
        val loading: Boolean = true,
        val data: StreamSimple? = null,
        val error: String? = null
    )
}
