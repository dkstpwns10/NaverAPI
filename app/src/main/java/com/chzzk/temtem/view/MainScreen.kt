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
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chzzk.temtem.service.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(appbarColor: Color, drawerColor: Color) {
    val streamViewModel: MainViewModel = viewModel()
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 150.dp, // The height when the sheet is collapsed
        sheetContent = {
            BottomSheet_StreamView(streamViewModel, scaffoldState)
        },
        sheetBackgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(appbarColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(appbarColor)
                    .padding(16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                //공지사항
                Card(modifier = Modifier.fillMaxSize()) {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = "공지사항",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = drawerColor
                        )
                    }
                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        count = 10,
                        state = pagerState
                    ) { pager ->
                        Column {
                            repeat(11) { index ->
                                Text(text = (index + 1).toString())
                            }
                        }
                    }
                }
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "바로가기 >", color = drawerColor,fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
            }

            Divider(
                modifier = Modifier
                    .width(350.dp)
                    .padding(8.dp),
                thickness = 1.dp,
                color = Color.Black
            )

            //알림, 위터
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //알림
                Card(
                    modifier = Modifier
                        .width(175.dp)
                        .height(300.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
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
                            Text(text = "바로가기 >", color = drawerColor,fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                }

                VerticalDivider(color = Color.Black)

                //위터
                Card(
                    modifier = Modifier
                        .width(175.dp)
                        .height(300.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
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
                            Text(text = "바로가기 >", color = drawerColor,fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    thickness: Dp = 1.dp,
    startIndent: Dp = 0.dp,
    endIndent: Dp = 0.dp
) {
    Box(
        modifier = modifier
            .width(thickness)
            .height(300.dp)
            .background(color)
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(Color.White, Color.Black)
}