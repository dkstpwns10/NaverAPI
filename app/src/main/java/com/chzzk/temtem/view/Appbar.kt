package com.chzzk.temtem.view

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
import androidx.compose.material3.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberModalBottomSheetState
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
import coil.compose.rememberAsyncImagePainter
import com.chzzk.temtem.R
import com.chzzk.temtem.service.MainViewModel
import com.chzzk.temtem.service.NaverLogin
import com.chzzk.temtem.service.StateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun topAppBar(
    viewModel: MainViewModel,
    context: Context,
    scope: CoroutineScope,
    stateViewmodel: StateViewModel
) {

    val appbarColor = remember {
        mutableStateOf(Color.White)
    }
    val drawerColor = remember {
        mutableStateOf(Color.Black)
    }

    val drawerState = rememberDrawerState(stateViewmodel.getDrawerState)

    val REDIRECT_URI = "http://172.30.1.42:5000/callback"

    Log.d("DrawerState", "Appbar Start DrawerState: ${stateViewmodel.getDrawerState}")
    BackOnPressed(context, stateViewmodel)
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
            stateViewmodel.setDrawerState(DrawerValue.Closed)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            DrawerContent(onCloseDrawer = {
                scope.launch {
                    drawerState.close()
                    stateViewmodel.setDrawerState(DrawerValue.Closed)
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
                                            //로고 클릭시 메인 스크린으로 이동
                                            /*TODO*/
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
                                    drawerState.open()
                                    stateViewmodel.setDrawerState(DrawerValue.Open)
                                    Log.d(
                                        "DrawerState",
                                        "DrawerButtonClicked: ${stateViewmodel.getDrawerState}"
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
                            IconButton(onClick = { /*TODO*/ }) {
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
                        // Main content goes here
                        val currnetScreen = remember {
                            mutableStateOf("Main")
                        }
                        MainScreen(
                            appbarColor = appbarColor.value,
                            drawerColor = drawerColor.value,
                            scope
                        )

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
                    Box(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable {
                                // 프로필 이미지 추가 로직 지금은 예시
                                profileImageUrl = R.drawable.image_channel
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUrl == null) {
                            Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(model = profileImageUrl),
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(80.dp),
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        viewModel._userState.value.data?.email?.let {
                            email = viewModel._userState.value.data?.email!!
                            Text(
                                text = email,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Image(painter = painterResource(id = R.drawable.btn_logout),
                            contentDescription = "Logout Button",
                            modifier = Modifier
                                .size(130.dp, 65.dp)
                                .padding(1.dp)
                                .clickable {
                                    naver.logout(context)
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
