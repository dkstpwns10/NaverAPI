package com.chzzk.temtem.service

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.chzzk.temtem.BuildConfig
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import android.content.SharedPreferences
import androidx.core.content.ContextCompat.startActivity
import com.chzzk.temtem.MainActivity
import com.chzzk.temtem.api.NaverUserProfileResponse
import com.chzzk.temtem.api.naverApiService
import com.chzzk.temtem.domain.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NaverLogin(private val context: Context,val viewModel: MainViewModel) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPreferences.edit()



    fun naverLogin(context: Context, viewModel: MainViewModel) {
        val client=BuildConfig.Naver_Client_Key
        val secret=BuildConfig.Naver_Secret_Key
        val clientName = "점례동화"

        NaverIdLoginSDK.initialize(this.context,client,secret,clientName)

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                Log.d("abcdefr", "AccessToken : " + NaverIdLoginSDK.getAccessToken())
                Log.d("test", "client id : " + client)
                Log.d("test", "ReFreshToken : " + NaverIdLoginSDK.getRefreshToken())
                Log.d("test", "Expires : " + NaverIdLoginSDK.getExpiresAt().toString())
                Log.d("test", "TokenType : " + NaverIdLoginSDK.getTokenType())
                Log.d("test", "State : " + NaverIdLoginSDK.getState().toString())

                autoLogin(NaverIdLoginSDK.getAccessToken())
                this@NaverLogin.viewModel.loginState(true)
                Log.d("loginState", "${this@NaverLogin.viewModel.isLogined.value}")

                editor.putString("NAVER_ACCESS_TOKEN", NaverIdLoginSDK.getAccessToken())
                editor.apply()

                Toast.makeText(this@NaverLogin.context, "로그인 성공", Toast.LENGTH_SHORT).show()
            }


            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("test", "$errorCode $errorDescription")
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(this.context, oauthLoginCallback)
    }

    fun autoLogin(token: String?){
        val accessToken = "Bearer $token"
        val call = naverApiService.getUserProfile(accessToken)
        call.enqueue(object : Callback<NaverUserProfileResponse> {
            override fun onResponse(
                call: Call<NaverUserProfileResponse>,
                response: Response<NaverUserProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val userProfile = response.body()?.response
                    if (userProfile != null) {
                        // 성공적으로 사용자 정보를 가져왔을 때 처리
                        Log.d("AutoLogin", "User userProfile: $userProfile")
                        Log.d("AutoLogin", "User ID: ${userProfile.id}")
                        Log.d("AutoLogin", "User Email: ${userProfile.email}")

                        userProfile?.let {
                            val user = User(
                                email = it.email,
                                id = it.id
                            )
                            viewModel._userState.value = viewModel._userState.value.copy(
                                data = user
                            )
                        }
                        viewModel.loginState(true)
                    } else {
                        Log.e("AutoLogin", "Failed to get user profile: Response body is null")
                        viewModel.loginState(false)
                    }
                } else {
                    Log.e("AutoLogin", "Failed to get user profile: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<NaverUserProfileResponse>, t: Throwable) {
                Log.e("AutoLogin", "API call failed: ${t.message}")
            }
        })
    }
    fun logout(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        val clientId = BuildConfig.Naver_Client_Key
        val clientSecret = BuildConfig.Naver_Secret_Key
        val accessToken = sharedPreferences.getString("NAVER_ACCESS_TOKEN", null) ?: return

        val baseUrl = "https://nid.naver.com/oauth2.0/token"
        val grantType = "delete"

        // 네이버 API를 사용하여 로그아웃 처리
        val call = naverApiService.logout(
            url = baseUrl,
            grantType = grantType,
            clientId = clientId,
            clientSecret = clientSecret,
            accessToken = accessToken
        )
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // 성공적으로 로그아웃 처리
                    Log.d("Logout", "Successfully logged out")

                    // SharedPreferences에서 토큰 및 사용자 정보 삭제
                    editor.remove("NAVER_ACCESS_TOKEN")
                    editor.apply()

                    // 로그아웃 후 필요한 다른 처리 (예: 로그인 화면으로 이동)
                    viewModel.loginState(false)
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(intent)
                    Toast.makeText(this@NaverLogin.context, "로그아웃 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("Logout", "Failed to logout: ${response.errorBody()?.string()}")
                }

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Logout", "API call failed: ${t.message}")
            }
        })
    }

}
