package com.chzzk.temtem.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chzzk.temtem.service.MainViewModel

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val streamViewModel: MainViewModel = viewModel()
    val viewDetailState by streamViewModel.streamDetailState
    val viewSimpleState by streamViewModel.streamSimpleState

    Column(modifier = Modifier.fillMaxSize().background(Color.Black), horizontalAlignment = Alignment.CenterHorizontally) {
        topAppBar()
    }

}