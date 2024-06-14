package com.chzzk.temtem.service

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.chzzk.temtem.BuildConfig
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

class NaverLogin(private val context: Context) {
    val isLoggedIn = mutableStateOf(false)
    val accessToken = mutableStateOf("")
    val refreshToken = mutableStateOf("")
    val expiresAt = mutableStateOf("")
    val tokenType = mutableStateOf("")
    val state = mutableStateOf("")
    val errorMessage = mutableStateOf("")

    init {
        NaverIdLoginSDK.initialize(
            context, // 네이버 개발자 센터에서 발급받은 Client ID
            BuildConfig.Naver_Client_Key, // 네이버 개발자 센터에서 발급받은 Client ID
            BuildConfig.Naver_Secret_Key, // 네이버 개발자 센터에서 발급받은 Client Secret
            "점례동화" // 앱 이름
        )
    }

    fun login() {
        val loginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 로그인 성공 시 토큰 정보 업데이트
                accessToken.value = NaverIdLoginSDK.getAccessToken() ?: ""
                refreshToken.value = NaverIdLoginSDK.getRefreshToken() ?: ""
                expiresAt.value = NaverIdLoginSDK.getExpiresAt().toString()
                tokenType.value = NaverIdLoginSDK.getTokenType() ?: ""
                state.value = NaverIdLoginSDK.getState().toString()
                isLoggedIn.value = true
            }

            override fun onFailure(httpStatus: Int, message: String) {
                // 로그인 실패 시 오류 정보 표시
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                errorMessage.value = "errorCode:$errorCode, errorDesc:$errorDescription"
                Toast.makeText(context, errorMessage.value, Toast.LENGTH_SHORT).show()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(context as Activity, loginCallback)
    }
}
