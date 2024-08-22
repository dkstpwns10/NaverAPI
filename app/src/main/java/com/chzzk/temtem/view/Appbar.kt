package com.chzzk.temtem.view

import SettingsDialog
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.dataStore
import coil.compose.rememberAsyncImagePainter
import com.chzzk.temtem.R
import com.chzzk.temtem.clearNaverAccessToken
import com.chzzk.temtem.service.MainViewModel
import com.chzzk.temtem.service.NaverLogin
import com.chzzk.temtem.service.StateViewModel
import com.chzzk.temtem.utils.SettingsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun topAppBar(
    viewModel: MainViewModel,
    context: Context,
    scope: CoroutineScope,
    stateViewmodel: StateViewModel
) {
    val settingState = remember {
        mutableStateOf(false)
    }
    val appbarColor = remember {
        mutableStateOf(Color.White)
    }
    val drawerColor = remember {
        mutableStateOf(Color.Black)
    }

    val drawerState2 = rememberDrawerState(DrawerValue.Closed)


    if(drawerState2.currentValue == DrawerValue.Open){
        stateViewmodel.setDrawerState(DrawerValue.Open)
    }else{
        stateViewmodel.setDrawerState(DrawerValue.Closed)
    }
    Log.d("DrawerState", "Appbar Start DrawerState: ${stateViewmodel.getDrawerState}")
    Log.d("DrawerState", "Appbar Start DrawerState2: ${drawerState2.currentValue}")
    BackOnPressed(context, stateViewmodel)
    BackHandler(enabled = drawerState2.isOpen) {
        scope.launch {
            drawerState2.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState2,
        drawerContent = {
            DrawerContent(onCloseDrawer = {
                scope.launch {
                    drawerState2.close()
                }
            }, viewModel, context)
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(appbarColor.value),
                        title = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(Alignment.Center)
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clickable(onClick = {
                                            viewModel.ScreenState.value = "Main"
                                        }),
                                    painter = painterResource(id = R.drawable.appbar),
                                    contentDescription = "",
                                    contentScale = ContentScale.FillHeight
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState2.open()
                                    Log.d(
                                        "DrawerState",
                                        "DrawerButtonClicked: ${stateViewmodel.getDrawerState}"
                                    )
                                    Log.d(
                                        "DrawerState",
                                        "DrawerButtonClicked drawerState2: ${drawerState2.currentValue}"
                                    )
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    tint = drawerColor.value,
                                    contentDescription = ""
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { settingState.value = true }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "",
                                    tint = drawerColor.value
                                )
                            }
                        }
                    )
                },
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 주석: 수정된 부분 - viewModel.ScreenState에 따라 화면 전환
                        when (viewModel.ScreenState.value) {
                            "Main" -> {
                                MainScreen(
                                    appbarColor = appbarColor.value,
                                    drawerColor = drawerColor.value,
                                    scope,viewModel,context
                                )
                            }

                        }
                        if(settingState.value){
                            SettingsDialog(
                                onDismiss = {
                                    settingState.value = false
                                },
                                settingsRepository = SettingsRepo()
                            )
                        }
                    }
                }
            )
        }
    )
}



@Composable
fun DrawerContent(
    onCloseDrawer: () -> Unit,
    viewModel: MainViewModel,
    context: Context
) {
    val naver = NaverLogin(context, viewModel)
    var email by remember {
        mutableStateOf("")
    }
    val drawerContainerColor = remember {
        mutableStateOf(Color(0xFFD3D3D3))
    }
    var profileImageUrl by remember {
        mutableStateOf<Int?>(null)
    }
    var isLoggedIn = viewModel.isLogined.value
    ModalDrawerSheet(
        modifier = Modifier.background(drawerContainerColor.value)
    ) {
        // 메뉴 아이템
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(350.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (!isLoggedIn) {
                    Button(onClick = {
                        naver.naverLogin(context, viewModel)

                    }) {
                        Text(text = "네이버로그인")
                    }

                } else {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(30.dp))
                        viewModel._userState.value.data?.let {
                            Text(
                                text = it.name!!,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            )
                            Text(
                                text = it.email!!,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Image(painter = painterResource(id = R.drawable.btn_logout),
                            contentDescription = "Logout Button",
                            modifier = Modifier
                                .size(130.dp, 65.dp)
                                .padding(1.dp)
                                .clickable {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        clearNaverAccessToken(context)
                                    }
                                    viewModel.loginState(false)
                                })
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Divider(modifier = Modifier.width(300.dp), thickness = 2.dp)
                Spacer(modifier = Modifier.size(50.dp))
                Text(text = "공지 사항", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
                Divider(modifier = Modifier.width(150.dp), thickness = 2.dp)
                Text(text = "탬알림", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
                Divider(modifier = Modifier.width(150.dp), thickness = 2.dp)
                Text(text = "탬위터", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
                Divider(modifier = Modifier.width(150.dp), thickness = 2.dp)
                Text(text = "게시글 작성", fontSize = 20.sp, modifier = Modifier.padding(16.dp))
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(text = "© 2024 Lurker. All rights reserved.\n", fontSize = 12.sp)
                Spacer(modifier = Modifier.size(0.5.dp))
                Text(text = "Developed by Lurker.", fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BackOnPressed(context: Context,stateViewmodel: StateViewModel) {
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L
    val bottomSheetState = rememberBottomSheetScaffoldState()

    BackHandler(enabled = backPressedState) {
        if (bottomSheetState.bottomSheetState.isCollapsed && stateViewmodel.getDrawerState == DrawerValue.Closed) {
            if (System.currentTimeMillis() - backPressedTime <= 400L) {
                // 앱 종료
                (context as Activity).finish()
            } else {
                backPressedState = true
                Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }
}