package com.chzzk.temtem.view


import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.viewpager2.widget.ViewPager2
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.chzzk.temtem.domain.AlarmData
import com.chzzk.temtem.domain.NoticeData
import com.chzzk.temtem.domain.WiterData
import com.chzzk.temtem.service.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    appbarColor: Color,
    drawerColor: Color,
    scope: CoroutineScope,
    viewModel: MainViewModel,
    context: Context
) {
    val noticePagerState = rememberPagerState()
    val alarmPagerState = rememberPagerState()
    val witerPagerState = rememberPagerState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val noticeData by viewModel.noticeData.collectAsState()
    val alarmData by viewModel.alarmData.collectAsState()
    val witerData by viewModel.witerData.collectAsState()
    val noticePageCount = (noticeData.size + 9) / 10
    val alarmPageCount = (alarmData.size + 9) / 10
    val witerPageCount = (witerData.size + 9) / 10

    LaunchedEffect(
        noticePagerState.currentPage,
        alarmPagerState.currentPage,
        witerPagerState.currentPage
    ) {
        viewModel.fetchNoticeData(noticePagerState.currentPage + 1)
        viewModel.fetchWiterData(witerPagerState.currentPage + 1)
        viewModel.fetchAlarmData(alarmPagerState.currentPage + 1)
    }
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 150.dp, // 바텀 시트 접혀있 을 때 높이
        sheetContent = {
            BottomSheet_StreamView(viewModel, scaffoldState, scope)
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
                    .height(280.dp)
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
                        count = noticePageCount,
                        state = noticePagerState
                    ) { page ->
                        val pageData = noticeData.drop(page * 10).take(10)
                        if (pageData.isEmpty()) {
                            Text(
                                "No notices available", modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize()
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Top
                            ) {
                                items(pageData) { notice ->
                                    NoticeItem(notice)

                                }
                            }
                        }
                    }
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
                Box {
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
                            TextButton(onClick = {
                                openNaverCafePage(context, "9")
                            }) {
                                Text(
                                    text = "바로가기 >",
                                    color = drawerColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                        HorizontalPager(
                            count = alarmPageCount,
                            state = alarmPagerState
                        ) { page ->
                            val pageData = alarmData.drop(page * 10).take(10)
                            if (pageData.isEmpty()) {
                                Text(
                                    "No alarms available", modifier = Modifier
                                        .fillMaxSize()
                                        .wrapContentSize()
                                )
                            } else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    items(pageData) { items ->
                                        AlarmItem(items)
                                    }
                                }
                            }
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
                        TextButton(onClick = {
                            openNaverCafePage(context, "10")
                        }) {
                            Text(
                                text = "바로가기 >",
                                color = drawerColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                    HorizontalPager(
                        count = witerPageCount,
                        state = witerPagerState
                    ) { page ->
                        val pageData = witerData.drop(page * 10).take(10)
                        if (pageData.isEmpty()) {
                            Text(
                                "No alarms available", modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentSize()
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Top
                            ) {
                                items(pageData) { items ->
                                    WiterItem(context,items)
                                }
                            }
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

@Composable
fun NoticeItem(notice: NoticeData) {
    val dateFormatter = remember { SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US) }
    val outputFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) }
    var showDialog by remember { mutableStateOf(false) }
    var scrollState = ScrollableState { 0.0f }
    if(showDialog){
        Dialog(onDismissRequest = { showDialog=false }) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))

                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.scrollable(scrollState, orientation = Orientation.Vertical),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = notice.name, fontWeight = FontWeight.Bold, fontSize = 16.sp,modifier = Modifier.padding(8.dp))
                    Text(text = notice.content,modifier = Modifier.padding(8.dp))
                }

            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        TextButton(onClick = { showDialog=true }) {
            Text(
                text = notice.name ?: "",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = notice.date?.let {
                    try {
                        val date = dateFormatter.parse(it)
                        outputFormatter.format(date)
                    } catch (e: ParseException) {
                        it // 파싱에 실패하면 원래 문자열 반환
                    }
                } ?: "",
                modifier = Modifier.padding(start = 4.dp)
            )
        }

    }
}

@Composable
fun AlarmItem(alarm: AlarmData) {
    val dateFormatter = remember { SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US) }
    val outputFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) }
    val stream = remember { mutableStateOf(alarm.stream) }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val imageUrl = if (stream.value == "YES") {
            "https://storep-phinf.pstatic.net/ogq_5e0787ada894b/original_1.png?type=p50_50"
        } else {
            "https://storep-phinf.pstatic.net/ogq_5e0787ada894b/original_2.png?type=p50_50"
        }
        val painter = rememberAsyncImagePainter(imageUrl)
        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(16.dp))
                    .width(250.dp)
                    .height(300.dp)
                    .background(Color.White)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    if (stream.value == "YES") {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp, 200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "오뱅있!", modifier = Modifier.padding(top = 8.dp), style = TextStyle(fontSize = 18.sp, color = Color.Black,fontWeight = FontWeight.Bold))

                    } else {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier.size(200.dp, 200.dp),
                            contentScale = ContentScale.Crop // 이미지의 크기와 비율 조정
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "오뱅없!",modifier = Modifier.padding(top = 8.dp),style = TextStyle(fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold))

                    }
                }

            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(onClick = {
            showDialog = true
        }) {
            Text(
                text = alarm.name ?: "",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = alarm.a_date?.let {
                    try {
                        val date = dateFormatter.parse(it)
                        outputFormatter.format(date)
                    } catch (e: ParseException) {
                        it // 파싱에 실패하면 원래 문자열 반환
                    }
                } ?: "",
            )

        }
    }
}

@Composable
fun WiterItem(context: Context,witer: WiterData) {
    val dateFormatter = remember { SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US) }
    val outputFormatter = remember { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()) }
    val url = remember { mutableStateOf(witer.url) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(onClick = {
            openNaverCafeWiter(context, witer.url)
        }) {
            Text(
                text = witer.name ?: "",
                modifier = Modifier.weight(1f)
            )
            Text(
                text = witer.a_date?.let {
                    try {
                        val date = dateFormatter.parse(it)
                        outputFormatter.format(date)
                    } catch (e: ParseException) {
                        it // 파싱에 실패하면 원래 문자열 반환
                    }
                } ?: "1",
            )

        }
    }
}
fun openNaverCafePage(context: Context, menuId: String) {
    val cafeAppUri = Uri.parse("navercafe://cafe/30688884/$menuId")
    val webUri = Uri.parse("https://m.cafe.naver.com/ca-fe/web/cafes/30688884/menus/$menuId")

    val intent = Intent(Intent.ACTION_VIEW, cafeAppUri)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // 앱이 설치되어 있지 않은 경우 웹 브라우저로 열기
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        context.startActivity(webIntent)
    }
}

fun openNaverCafeWiter(context: Context, url: String) {
    val url = Uri.parse(url)
    val articleId = url.getQueryParameter("articleid")
    val cafeAppUri = Uri.parse("navercafe://cafe/30688884/10/$articleId")
    val webUri = Uri.parse("https://m.cafe.naver.com/ca-fe/web/cafes/30688884/articles/$articleId?fromList=true&menuId=10&tc=cafe_article_list")

    val intent = Intent(Intent.ACTION_VIEW, cafeAppUri)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // 앱이 설치되어 있지 않은 경우 웹 브라우저로 열기
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        context.startActivity(webIntent)
    }
}
