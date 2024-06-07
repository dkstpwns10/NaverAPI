package com.chzzk.temtem.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.chzzk.temtem.service.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(appbarColor: Color, drawerColor: Color) {
    val streamViewModel: MainViewModel = viewModel()
    val viewDetailState by streamViewModel.streamDetailState
    val viewSimpleState by streamViewModel.streamSimpleState
    val now_Streaming = remember {
        false
    }
    val pagerState = rememberPagerState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appbarColor), horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(appbarColor)
                .padding(16.dp), contentAlignment = Alignment.TopEnd
        ) {
            Card(modifier = Modifier.fillMaxSize()) {
                //Card
                if (!now_Streaming) {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "공지사항",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = drawerColor
                        )
                    }
                    HorizontalPager(modifier = Modifier.fillMaxSize(), count = 10, state = pagerState) {
                        //카페 공지 사항 끌고 오기
                        pager->
                        Column {
                            Text(text = "1")
                            Text(text = "2")
                            Text(text = "3")
                            Text(text = "4")
                            Text(text = "5")
                            Text(text = "6")
                            Text(text = "7")
                            Text(text = "8")
                            Text(text = "9")
                            Text(text = "10")
                            Text(text = "11")
                        }

                    }


                } else {
                    //방송중이면 방송 썸네일 + 링크
                }
            }
            //Box
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "더보기 >", color = drawerColor)
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Divider(modifier = Modifier.width(350.dp), thickness = 1.dp, color = Color.Black)
        Spacer(modifier = Modifier.height(10.dp))
        //Column
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Card(
                modifier = Modifier
                    .width(175.dp)
                    .height(300.dp)
            ) {

                Row {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "탬 알림",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = drawerColor
                        )
                    }
                    Spacer(modifier = Modifier.width(45.dp))
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "더보기 >", color = drawerColor, fontSize = 10.sp)
                    }
                }

            }
            Card(
                modifier = Modifier
                    .width(175.dp)
                    .height(300.dp)
            ) {
                Row {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "탬 위터",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = drawerColor
                        )
                    }
                    Spacer(modifier = Modifier.width(45.dp))
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "더보기 >", color = drawerColor, fontSize = 10.sp)
                    }
                }

            }
        }
    }

}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(Color.White, Color.Black)
}