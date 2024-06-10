package com.chzzk.temtem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chzzk.temtem.service.MainViewModel
import com.chzzk.temtem.ui.theme.TemtemTheme
import com.chzzk.temtem.view.topAppBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemtemTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val streamViewmodel =  viewModel<MainViewModel>()
                    streamViewmodel.getStreamDetail()
                    val scope = rememberCoroutineScope()
                    LaunchedEffect(Unit) {
                        streamViewmodel.getStreamData(scope)
                    }
                    topAppBar(streamViewmodel,scope)
                }
            }
        }
    }
}



