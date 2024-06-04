package com.chzzk.temtem.service

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chzzk.temtem.api.streamService
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


    private fun fetchStreamDetail(){
        viewModelScope.launch {
            try {
                val response = streamService.getDetailStream()
                _streamDetailState.value = _streamDetailState.value.copy(
                    data = response,
                    loading = false,
                    error = null
                )
            }catch (e:Exception){
                _streamDetailState.value = streamDetailState.value.copy(loading = false, error = "Fetching Error : ${e.message}")
            }
        }
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
        val data : StreamDetail? = null,
        val error : String? = null
    )

    data class StreamSimpleState(
        val loading : Boolean = true,
        val data : StreamSimple? = null,
        val error : String? = null
    )
}