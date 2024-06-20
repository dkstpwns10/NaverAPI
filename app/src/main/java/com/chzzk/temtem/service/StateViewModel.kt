package com.chzzk.temtem.service

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

@SuppressLint("UnrememberedMutableState")
class StateViewModel : ViewModel() {
    // 내부에서 관리하는 drawerState
    private val _drawerState = mutableStateOf(DrawerValue.Closed)

    // 외부에서 접근할 수 있는 drawerState
    val getDrawerState: DrawerValue
        get() = _drawerState.value

    fun setDrawerState(newState:DrawerValue) {
        _drawerState.value = newState
    }
}

