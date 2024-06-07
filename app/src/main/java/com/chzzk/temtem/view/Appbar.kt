package com.chzzk.temtem.view

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.chzzk.temtem.R
import com.chzzk.temtem.service.NaverLogin
import kotlinx.coroutines.launch

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topAppBar() {
    val context = LocalContext.current
    val appbarColor = remember {
        mutableStateOf(Color.White)
    }
    val drawerColor = remember {
        mutableStateOf(Color.Black)
    }
    val drawerContainerColor = remember {
        mutableStateOf(Color(0xFFD3D3D3))
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isLoggedIn by remember{
        mutableStateOf(false)
    }
    var profileImageUrl by remember {
        mutableStateOf<Int?>(null)
    }
    val naver = remember{NaverLogin(context)}

    // 메뉴창

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
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
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            if (!isLoggedIn) {
                                IconButton(modifier = Modifier.size(200.dp,400.dp), onClick = {
                                    //isLoggedIn = true
                                }

                                ) {
                                    Image(painter = painterResource(id = R.drawable.btn_complete),modifier=Modifier.size(200.dp,400.dp), contentScale = ContentScale.FillWidth,contentDescription = "로그인 버튼")
                                }
                            }else{
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
                                Column {
                                    Text(text = "곽철용", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(text = "naevr@naver.com", fontSize = 12.sp,textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth())
                                }
                            }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Divider(modifier = Modifier.width(300.dp), thickness = 2.dp)
                        Spacer(modifier = Modifier.size(50.dp))
                        Text(text = "공지 사항", fontSize = 20.sp,modifier = Modifier.padding(16.dp))
                        Divider(modifier = Modifier.width(150.dp), thickness = 2.dp)
                        Text(text = "탬알림", fontSize = 20.sp,modifier = Modifier.padding(16.dp))
                        Divider(modifier = Modifier.width(150.dp), thickness = 2.dp)
                        Text(text = "탬위터", fontSize = 20.sp,modifier = Modifier.padding(16.dp))
                        Divider(modifier = Modifier.width(150.dp), thickness = 2.dp)
                        Text(text = "게시글 작성", fontSize = 20.sp,modifier = Modifier.padding(16.dp))

                    }

                    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.BottomCenter) {
                        Text(text = "© 2024 Lurker. All rights reserved.\n",fontSize = 12.sp)
                        Spacer(modifier = Modifier.size(0.5.dp))
                        Text(text = "Developed by Lurker.",fontSize = 12.sp)
                    }


                }
            }
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
                                    modifier = Modifier.size(100.dp).clickable(onClick = {
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
                        // Main content goes here]
                        val currnetScreen = remember{
                            mutableStateOf("Main")
                        }
                        MainScreen(appbarColor.value,drawerColor.value)

                    }
                }
            )
        }
    )
}

@Composable
@Preview
fun PreviewAppBar() {
    topAppBar()
}
