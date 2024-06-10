package com.chzzk.temtem.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chzzk.temtem.R
import com.chzzk.temtem.domain.DetailContent
import com.chzzk.temtem.service.MainViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun BottomSheet_StreamView(viewModel: MainViewModel,bottomSheetState: BottomSheetScaffoldState) {
    val domainData by viewModel.streamDetailState
    var detailContent: DetailContent? = domainData.data
    val scope = rememberCoroutineScope()

    val fontColor = remember {
        mutableStateOf(Color(115,68,64))
    }
    val liveState = remember {
        mutableStateOf("방송중")
    }
    val liveIcon = remember {
        mutableStateOf(R.drawable.broadcasting)
    }
    val bottomImage = remember{
        mutableStateOf(R.drawable.window_round)
    }
    if (detailContent != null) {
        when (detailContent.status) {
            "OPEN" -> {
                fontColor.value = Color(115,68,64)
                liveState.value = "방송중!"
                liveIcon.value = R.drawable.broadcasting
                bottomImage.value = R.drawable.window_round
            }

            "CLOSE" -> {
                fontColor.value = Color(242,231,80)
                liveState.value = "오프라인"
                liveIcon.value = R.drawable.off
                bottomImage.value = R.drawable.sq_round
            }
        }
    } else {
        fontColor.value = Color(217,161,113)
        liveState.value = "오프라인"
        liveIcon.value = R.drawable.off
        bottomImage.value = R.drawable.sq_round
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
                BottomSheetDefaults.DragHandle()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(modifier = Modifier.size(45.dp), painter = painterResource(id = bottomImage.value), contentDescription = "")
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
                Divider(modifier = Modifier.width(370.dp), color = Color.Black)
                StreamContent(onHideButtonClick = {
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
fun StreamContent(onHideButtonClick: () -> Unit) {
    val bottomInsets = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = bottomInsets
        )
    ) {
        items(10) {
            ListItem(
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                    Text("item $it")
                }

            }
        }
        item {
            Button(modifier = Modifier.fillMaxWidth(), onClick = { onHideButtonClick() }) {
                Text("Cancel")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun StreamContentPreview() {
    val streamViewModel: MainViewModel = viewModel()
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheet_StreamView(viewModel = streamViewModel,scaffoldState)
}