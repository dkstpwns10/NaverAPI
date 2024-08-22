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
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chzzk.temtem.service.MainViewModel
import com.chzzk.temtem.service.NaverLogin
import com.chzzk.temtem.service.StateViewModel
import com.chzzk.temtem.ui.theme.TemtemTheme
import com.chzzk.temtem.utils.AppDataStore
import com.chzzk.temtem.utils.AppDataStore.Companion.dataStore
import com.chzzk.temtem.utils.PreferencesKeys
import com.chzzk.temtem.utils.PreferencesKeys.NAVER_ACCESS_TOKEN
import com.chzzk.temtem.utils.SettingsRepo
import com.chzzk.temtem.utils.getNaverAccessToken
import com.chzzk.temtem.view.topAppBar
import com.chzzk.temtem.utils.subscribeAllTopics
import com.chzzk.temtem.utils.subscribeTopics
import com.chzzk.temtem.utils.unsubscribeTopic
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.i(TAG, "Notification permission granted")
            } else {
                Log.i(TAG, "Notification permission denied")
            }
        }
    private val settingsRepo = SettingsRepo()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemtemTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val topics = listOf("broadcast_alert", "tem_alarm", "tem_twitter", "notice")
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



                    LaunchedEffect(Unit) {
                        settingsRepo.initializeAllTopics()
                        initializeFCMTopics(topics, settingsRepo = settingsRepo)
                        streamViewmodel.fetchLiveStatus()
                        val accessToken = getToken()
                        Log.d("MainActivity", "NAVER_ACCESS_TOKEN: $accessToken")
                        if (accessToken != null) {
                            streamViewmodel.loginState(true)
                            naver.fetch_profile(accessToken)
                        } else {
                            streamViewmodel.loginState(false)
                            //naver.naverLogin(context,streamViewmodel)
                        }
                    }
                    topAppBar(streamViewmodel, context, scope, stateViewmodel)
                }
            }
        }
    }

    private suspend fun getToken(): String? {
        val preferences = dataStore.data.first()[PreferencesKeys.getNaverAccessToken()]
        Log.d("MainActivity", "getToken: $preferences")
        return preferences
    }
}

suspend fun clearNaverAccessToken(context: Context) {
    withContext(Dispatchers.IO) {
        dataStore.edit { preferences ->
            preferences.remove(NAVER_ACCESS_TOKEN)
        }

    }
}

private fun initializeFCMTopics(topics: List<String>, settingsRepo: SettingsRepo) {
    CoroutineScope(Dispatchers.IO).launch {
        for (topic in topics) {
            val state = settingsRepo.getNotificationState(topic)
            if (state) {
                subscribeTopics(topic)
            } else {
                unsubscribeTopic(topic)
            }

        }
    }
}





