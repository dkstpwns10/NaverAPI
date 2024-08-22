package com.chzzk.temtem.view

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.chzzk.temtem.R
import com.chzzk.temtem.domain.StatusContent
import com.chzzk.temtem.service.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BottomSheet_StreamView(viewModel: MainViewModel, bottomSheetState: BottomSheetScaffoldState,scope: CoroutineScope) {
    val streamStatus by viewModel.streamStatusState
    var detailContent: StatusContent? = streamStatus.data

    val fontColor = remember {
        mutableStateOf(Color(115, 68, 64))
    }
    val liveState = remember {
        mutableStateOf("방송중")
    }
    val liveIcon = remember {
        mutableStateOf(R.drawable.broadcasting)
    }
    val bottomImage = remember {
        mutableStateOf(R.drawable.window_round)
    }
    if (detailContent != null) {
        when (detailContent.status) {
            "OPEN" -> {
                fontColor.value = Color(115, 68, 64)
                liveState.value = "방송중!"
                liveIcon.value = R.drawable.broadcasting
                bottomImage.value = R.drawable.window_round
            }

            "CLOSE" -> {
                fontColor.value = Color(217, 161, 113)
                liveState.value = "오프라인"
                liveIcon.value = R.drawable.off
                bottomImage.value = R.drawable.sq_round
            }
        }
    } else {
        fontColor.value = Color(217, 161, 113)
        liveState.value = "오프라인"
        liveIcon.value = R.drawable.off
        bottomImage.value = R.drawable.sq_round
    }

    BackHandler(enabled = bottomSheetState.bottomSheetState.isExpanded) {
        scope.launch {
            bottomSheetState.bottomSheetState.collapse()
        }
    }
    Surface(
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        elevation = 20.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BottomSheetDefaults.DragHandle(modifier = Modifier.width(100.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(45.dp),
                        painter = painterResource(id = bottomImage.value),
                        contentDescription = ""
                    )
                    Text(
                        text = "탬탬버린은 지금 ${liveState.value}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = fontColor.value
                    )
                    Image(
                        modifier = Modifier.height(60.dp),
                        painter = painterResource(id = liveIcon.value),
                        contentDescription = null
                    )
                }
                Divider(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(8.dp), color = Color.Black
                )
                StreamContent(detailContent, onHideButtonClick = {
                    scope.launch {
                        bottomSheetState.bottomSheetState.collapse()
                    }
                })
            }

        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StreamContent(detailContent: StatusContent?, onHideButtonClick: () -> Unit) {
    val bottomInsets = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val liveTitle = remember {
        mutableStateOf("")
    }
    val imageLive = remember {
        mutableStateOf("https://livecloud-thumb.akamaized.net/chzzk/livecloud/KR/stream/26449561/live/6336879/record/28799867/thumbnail/image_720.jpg")
    }
    val iamgeOffline = remember {
        mutableStateOf(R.drawable.off_line)
    }

    val live_url = "https://chzzk.naver.com/live/a7e175625fdea5a7d98428302b7aa57f"
    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = bottomInsets
        )
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.size(400.dp, 200.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        if (detailContent != null) {
                            when (detailContent.status) {
                                "OPEN" -> {
                                    liveTitle.value = detailContent.liveTitle
                                    Image(
                                        painter = rememberAsyncImagePainter(imageLive.value),
                                        contentDescription = "Live Thumbnail",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                                    data = Uri.parse(live_url)
                                                }
                                                context.startActivity(intent)
                                            }
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Black.copy(alpha = 0.5f))
                                    )
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .size(48.dp)
                                            .background(
                                                Color.Black.copy(alpha = 0.7f),
                                                shape = CircleShape
                                            )
                                            .padding(8.dp)
                                    )
                                }

                                "CLOSE" -> {
                                    liveTitle.value = "지금은 방송 중이 아닙니다!"
                                    Image(
                                        painter = painterResource(id = R.drawable.off_line),
                                        contentDescription = "Live Off",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                                    data = Uri.parse(live_url)
                                                }
                                                context.startActivity(intent)
                                            }
                                    )

                                }
                            }

                        } else {
                            liveTitle.value = "현재 방송 정보를 받아 올 수 없습니다."
                            Image(
                                painter = painterResource(id = R.drawable.off_line),
                                contentDescription = "Live Off",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW).apply {
                                            data = Uri.parse(live_url)
                                        }
                                        context.startActivity(intent)
                                    }
                            )
                        }
                    }
                }
                Text(
                    text = liveTitle.value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
                Divider(
                    color = Color.Black, modifier = Modifier
                        .width(300.dp)
                        .padding(8.dp)
                )
                if (detailContent != null) {
                    when (detailContent.status) {
                        //온에어
                        "OPEN" -> {
//                            var duration = DateCalculater(detailContent.openDate)
//                            val hours = duration.toHours()
//                            val minutes = duration.toMinutes() % 60
//                            val seconds = duration.toSeconds() % 60
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Text(
                                        text = "카테고리",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = "${detailContent.liveCategoryValue}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                                VerticalDivider(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .padding(4.dp), color = Color.Black
                                )
//                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(4.dp)) {
//                                Text(text = "방송시간", fontWeight = FontWeight.Bold, fontSize = 10.sp)
//                                Text(text = "$hours:$minutes:$seconds", fontWeight = FontWeight.Bold, fontSize = 10.sp)
//                            }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Text(
                                        text = "현재 시청자",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = detailContent.concurrentUserCount.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }
                            }


                        }
                        //방종상태
                        "CLOSE" -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
//                                val open = detailContent.openDate
//                                val close = detailContent.closeDate
//                                val live_time : List<String> = live_Open_Close(open,close)
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(100.dp)
                                ) {
                                    Text(
                                        text = "마지막 카테고리",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = "${detailContent.liveCategoryValue}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(4.dp)
                                    )
//                                    Text(text = live_time.get(0), fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(4.dp))
//                                    Text(text = live_time.get(1), fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(4.dp))
                                }
                                VerticalDivider(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .padding(4.dp), color = Color.Black
                                )
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(100.dp)
                                ) {
                                    Text(
                                        text = "마지막 방송태그",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                    Text(
                                        text = "${detailContent.tags}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(4.dp)
                                    )
//                                    Text(text = live_time.get(2), fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(4.dp))
//                                    Text(text = live_time.get(3), fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(4.dp))
                                }
                            }
                        }
                    }
                    //detailContent가 NULL일 때
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(4.dp)
                                .width(100.dp)
                        ) {
                            Text(text = "카테고리", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Text(
                                text = "에러",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                        VerticalDivider(
                            modifier = Modifier
                                .height(50.dp)
                                .padding(4.dp), color = Color.Black
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(4.dp)
                                .width(100.dp)
                        ) {
                            Text(text = "방송시간", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Text(
                                text = "에러",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }

/////////////////////////////////////////////////////////////////////
        item {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(Color(115, 68, 64)),
                onClick = { onHideButtonClick() }) {
                Text("돌아가기", color = Color(242, 231, 80), fontWeight = FontWeight.Bold)
            }
        }
    }
}


fun DateCalculater(live_open: String?): Duration {
    val now = LocalDateTime.now()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val liveOpenDate =
        dateFormat.parse(live_open).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    val duration = Duration.between(now, liveOpenDate)
    return duration
}

fun live_Open_Close(live_open: String?, live_close: String?): List<String> {
    val open = live_open?.split(" ")
    val close = live_close?.split(" ")

    val date: MutableList<String> = mutableListOf()
    if (open != null && close != null) {
        date.add(open[0])
        date.add(open[1])
        date.add(close[0])
        date.add(close[1])
    }
    return date
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun StreamContentPreview() {
//    val streamViewModel: MainViewModel = viewModel()
//    val scaffoldState = rememberBottomSheetScaffoldState()
//    BottomSheet_StreamView(viewModel = streamViewModel, scaffoldState,scope)
}