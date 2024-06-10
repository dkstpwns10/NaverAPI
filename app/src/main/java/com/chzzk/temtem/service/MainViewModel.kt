package com.chzzk.temtem.service

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chzzk.temtem.api.streamService
import com.chzzk.temtem.domain.DetailContent
import com.chzzk.temtem.domain.StreamDetail
import com.chzzk.temtem.domain.StreamSimple
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _streamDetailState = mutableStateOf(StreamDetailState())
    val streamDetailState : State<StreamDetailState> = _streamDetailState

    private val _streamSimpleState = mutableStateOf(StreamSimpleState())
    val streamSimpleState : State<StreamSimpleState> = _streamSimpleState

    init {
        fetchStreamSimple()
        fetchStreamDetail()
    }


    private fun fetchStreamDetail() {
        viewModelScope.launch {
            try {
                val response = streamService.getDetailStream()
                if (response.code==200) {
                    val detailContent = response.content
                    // Retrofit이 서버 응답을 객체로 변환할 때 null이 아닌지 확인합니다.
                    detailContent?.let {
                        val streamDetail = DetailContent(
                            liveId = it.liveId,
                            liveTitle  = it.liveTitle,
                            status = it.status,
                            liveImageUrl  = it.liveImageUrl,
                            defaultThumbnailImageUrl = it.defaultThumbnailImageUrl,
                            concurrentUserCount = it.concurrentUserCount,
                            accumulateCount = it.accumulateCount,
                            openDate = it.openDate,
                            closeDate = it.closeDate,
                            adult = it.adult,
                            clipActive = it.clipActive,
                            tags = it.tags,
                            categoryType = it.categoryType,
                            liveCategory = it.liveCategory,
                            liveCategoryValue = it.liveCategoryValue,
                            chatActive = it.chatActive,
                            chatAvailableGroup = it.chatAvailableGroup,
                            paidPromotion = it.paidPromotion,
                            chatAvailableCondition = it.chatAvailableCondition,
                            minFollowerMinute = it.minFollowerMinute,
                            livePlaybackJson = it.livePlaybackJson,
                            p2pQuality = it.p2pQuality,
                            channel = it.channel,
                            chatDonationRankingExposure = it.chatDonationRankingExposure
                        )
                        _streamDetailState.value = _streamDetailState.value.copy(
                            data = streamDetail,
                            loading = false,
                            error = null,
                            errorMsg = response.message
                        )
                    } ?: run {
                        // 서버 응답이 null인 경우 에러 처리
                        _streamDetailState.value = _streamDetailState.value.copy(
                            loading = false,
                            error = "Response body is null."
                        )
                    }
                } else {
                    // 서버 응답이 실패한 경우 에러 처리
                    _streamDetailState.value = _streamDetailState.value.copy(
                        loading = false,
                        error = "Failed to fetch stream detail: ${response.message}"
                    )
                }
            } catch (e: Exception) {
                // 네트워크 오류 또는 예외 발생 시 에러 처리
                _streamDetailState.value = _streamDetailState.value.copy(
                    loading = false,
                    error = "Fetching Error: ${e.message}"
                )
            }
        }
    }
    fun getStreamData():State<StreamDetailState>{
        fetchStreamDetail()
        return streamDetailState
    }
    private fun fetchStreamSimple(){
        viewModelScope.launch {
            try {
                val response = streamService.getSimpleStream()
                _streamSimpleState.value = _streamSimpleState.value.copy(
                    data = response,
                    loading = false,
                    error = null
                )
            }catch (e:Exception){
                _streamSimpleState.value = streamSimpleState.value.copy(loading = false, error = "Fetching Error : ${e.message}")
            }
        }
    }

    data class StreamDetailState(
        val loading : Boolean = true,
        val data : DetailContent? = null,
        val error : String? = null,
        val errorMsg : String? =null
    )

    data class StreamSimpleState(
        val loading : Boolean = true,
        val data : StreamSimple? = null,
        val error : String? = null
    )
}