package com.chzzk.temtem.service

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chzzk.temtem.R

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val streamViewModel: MainViewModel = viewModel()
    val viewDetailState by streamViewModel.streamDetailState
    val viewSimpleState by streamViewModel.streamSimpleState


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBar() {
    val name by remember {
        mutableStateOf("점례동화")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(painter = painterResource(id = R.drawable.appbar_thin) , contentDescription = "")
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Menu, tint = Color.White, contentDescription = "" )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text=name,color= Color.Black, fontSize = 18.sp)
            }
        }
    )
}

//@Composable
//@Preview
//fun previewAppbar(){
//    topAppBar()
//}