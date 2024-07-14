package com.chzzk.temtem

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chzzk.temtem.service.MainViewModel
import com.chzzk.temtem.service.NaverLogin
import com.chzzk.temtem.service.StateViewModel
import com.chzzk.temtem.ui.theme.TemtemTheme
import com.chzzk.temtem.utils.AppDataStore.Companion.dataStore
import com.chzzk.temtem.utils.PreferencesKeys
import com.chzzk.temtem.view.topAppBar
import com.chzzk.temtem.utils.subscribeAllTopics
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.i(TAG, "Notification permission granted")
            } else {
                Log.i(TAG, "Notification permission denied")
            }
        }

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemtemTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val streamViewmodel = viewModel<MainViewModel>()
                    val stateViewmodel: StateViewModel = viewModel()

                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()

                    val naver = NaverLogin(context, streamViewmodel)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        when {
                            ContextCompat.checkSelfPermission(
                                this,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                // Permission is already granted
                                Log.i(TAG, "Notification permission already granted")
                            }
                            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                                // Display an educational UI to the user
                                Log.i(TAG, "Displaying notification permission rationale")
                                // Here, you could show an educational UI explaining why the permission is needed
                                // and then request the permission again
                                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                            else -> {
                                // Directly request for required permission
                                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }
                    }
                    // 저장된 네이버 토큰 가져오기
                    val accessToken: String? =
                        lifecycleScope.launch {
                            val token = getToken()
                            Log.d("MainActivity", "NAVER_ACCESS_TOKEN: $token")
                            // token을 사용하여 필요한 작업 수행
                        }.toString()
                    Log.d("MainActivityAccessToken", "Failed to get user profile: $accessToken")
                    if (accessToken != null) {
                        // 토큰이 존재하면 자동 로그인 처리
                        naver.autoLogin(accessToken)
                    }

                    subscribeAllTopics()

                    LaunchedEffect(Unit) {
                        streamViewmodel.fetchLiveStatus()
                    }
                    topAppBar(streamViewmodel, context, scope, stateViewmodel)

                }
            }
        }
    }
    private suspend fun getToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.NAVER_ACCESS_TOKEN]
    }
}





