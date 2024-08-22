package com.chzzk.temtem.service

import android.app.Application
import android.util.Log
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.chzzk.temtem.api.RetrofitClient
import com.chzzk.temtem.api.RetrofitDB
import com.chzzk.temtem.domain.AdParameter
import com.chzzk.temtem.domain.AlarmData
import com.chzzk.temtem.domain.Channel
import com.chzzk.temtem.domain.DetailContent
import com.chzzk.temtem.domain.NoticeData
import com.chzzk.temtem.domain.StatusContent
import com.chzzk.temtem.domain.StreamSimple
import com.chzzk.temtem.domain.User
import com.chzzk.temtem.domain.WiterData
import com.chzzk.temtem.utils.DBRepo
import com.chzzk.temtem.utils.isNetworkAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _streamDetailState = mutableStateOf(StreamDetailState())
    val streamDetailState: State<StreamDetailState> = _streamDetailState
    private val _isLogined = mutableStateOf(false)
    val isLogined: State<Boolean> = _isLogined
    private val _streamSimpleState = mutableStateOf(StreamSimpleState())
    val streamSimpleState: State<StreamSimpleState> = _streamSimpleState

    private val _streamStatusState = mutableStateOf(StreamStatusState())
    val streamStatusState: State<StreamStatusState> = _streamStatusState

    val _userState = mutableStateOf(UserState())
    val userState: State<UserState> = _userState

    val ScreenState = mutableStateOf("Main")

    fun loginState(state : Boolean){
        _isLogined.value = state
    }

//Status
    fun fetchLiveStatus() {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            if (!isNetworkAvailable(context)) {
                _streamStatusState.value = _streamStatusState.value.copy(
                    loading = false,
                    error = "Network is not available"
                )
                return@launch
            }

            Log.d("fetchLiveDetail", "fetchLiveDetail launched")
            _streamStatusState.value = _streamStatusState.value.copy(loading = true)
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

//DetailStream
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
                val apiService = RetrofitClient.api
                val response = apiService.getDetailStream()
                val stream = response.content
                stream?.let {
                    val detailContent = DetailContent(
                    accumulateCount= it.accumulateCount,
                    adParameter= it.adParameter,
                    adult=it.adult,
                    categoryType=it.categoryType,
                    channel=it.channel,
                    chatActive=it.chatActive,
                    chatAvailableCondition=it.chatAvailableCondition,
                    chatAvailableGroup=it.chatAvailableGroup,
                    chatChannelId=it.chatChannelId,
                    chatDonationRankingExposure=it.chatDonationRankingExposure,
                    clipActive=it.clipActive,
                    closeDate=it.closeDate,
                    concurrentUserCount=it.concurrentUserCount  ,
                    defaultThumbnailImageUrl=it.defaultThumbnailImageUrl,
                    liveCategory=it.liveCategory,
                    liveCategoryValue=it.liveCategoryValue,
                    liveId=it.liveId,
                    liveImageUrl=it.liveImageUrl,
                    livePlaybackJson=it.livePlaybackJson,
                    livePollingStatusJson=it.livePollingStatusJson,
                    liveTitle=it.liveTitle,
                    minFollowerMinute=it.minFollowerMinute,
                    openDate=it.openDate,
                    p2pQuality=it.p2pQuality,
                    paidPromotion=it.paidPromotion,
                    status=it.status,
                    tags=it.tags,
                    userAdultStatus=it.userAdultStatus,
                    )

                    _streamDetailState.value = _streamDetailState.value.copy(
                        data = detailContent,
                        loading = false,
                        error = null
                    )

                    Log.d("로그", "liveId: ${detailContent.liveTitle}")
                    Log.d("로그", "status: ${detailContent.status}")
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

    data class UserState(
        val data : User? = null,
    )

    private val repoDB = DBRepo()

    private val _alarmData = MutableStateFlow<List<AlarmData>>(emptyList())
    val alarmData: StateFlow<List<AlarmData>> = _alarmData

    fun fetchAlarmData(pageNumber: Int) {
        viewModelScope.launch {
            try {
                val data = repoDB.getAlarmData(pageNumber)
                _alarmData.value = data
                Log.d("MainViewModel", "alarm fetch성공")
            } catch (e: Exception) {
                // Handle exception
                Log.e("MainViewModel", "Error fetching alarm data : $e", e)
            }
        }
    }
    private val _witerData = MutableStateFlow<List<WiterData>>(emptyList())
    val witerData: StateFlow<List<WiterData>> = _witerData

    fun fetchWiterData(pageNumber: Int) {
        viewModelScope.launch {
            try {
                val data = repoDB.getWiterData(pageNumber)
                _witerData.value = data
                Log.d("MainViewModel", "witer fetch성공")
            } catch (e: Exception) {
                // Handle exception
                Log.e("MainViewModel", "Error fetching witer data : $e", e)
            }
        }
    }

    private val _noticeData = MutableStateFlow<List<NoticeData>>(emptyList())
    val noticeData: StateFlow<List<NoticeData>> = _noticeData
    fun fetchNoticeData(pageNumber: Int) {
        viewModelScope.launch {
            try {
                val data = repoDB.getNoticeData(pageNumber)
                _noticeData.value = data
                Log.d("MainViewModel", "notice fetch 성공")
            } catch (e: Exception) {
                // Handle exception
                Log.e("MainViewModel", "Error fetching notice data : $e", e)
            }
        }
    }

}
