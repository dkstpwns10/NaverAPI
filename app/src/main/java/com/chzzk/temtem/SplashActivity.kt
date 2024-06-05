package com.chzzk.temtem

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.window.SplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chzzk.temtem.ui.theme.TemtemTheme
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemtemTheme {
                SplashScreen(){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }


    @Composable
    private fun SplashScreen(modifier: Modifier=Modifier,onTimeOut:()->Unit) {

        var isLoading by remember { mutableStateOf(true) }
        val alpha = remember {
            Animatable(0f)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(250.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }
        Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.BottomCenter) {
            Text(text = "© 2024 Lurker. All rights reserved.\n",fontSize = 12.sp)
            Spacer(modifier = Modifier.size(0.5.dp))
            Text(text = "Developed by Lurker.",fontSize = 12.sp)
        }
        LaunchedEffect(Unit) {
            alpha.animateTo(1f, animationSpec = tween(1500))
            delay(2000)
            isLoading = false
            onTimeOut()
        }
    }
}

