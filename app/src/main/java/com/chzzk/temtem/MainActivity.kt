package com.chzzk.temtem

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chzzk.temtem.service.MainViewModel
import com.chzzk.temtem.service.NaverLogin
import com.chzzk.temtem.service.StateViewModel
import com.chzzk.temtem.ui.theme.TemtemTheme
import com.chzzk.temtem.view.topAppBar

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemtemTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val streamViewmodel = viewModel<MainViewModel>()
                    val stateViewmodel : StateViewModel = viewModel()
                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()
                    val naver = NaverLogin(context, streamViewmodel)
                    val sharedPreferences: SharedPreferences =
                        getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

                    // 저장된 토큰 가져오기
                    val accessToken: String? =
                        sharedPreferences.getString("NAVER_ACCESS_TOKEN", null)
                    Log.d("MainActivityAccessToken", "Failed to get user profile: $accessToken")
                    if (accessToken != null) {
                        // 토큰이 존재하면 자동 로그인 처리
                        naver.autoLogin(accessToken)

                    }
                    LaunchedEffect(Unit) {
                        streamViewmodel.fetchLiveStatus()
                    }
                    topAppBar(streamViewmodel, context,scope,stateViewmodel)

                }
            }
        }
    }

}





