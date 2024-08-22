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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chzzk.temtem.MainActivity
import com.chzzk.temtem.api.NaverUserProfileResponse
import com.chzzk.temtem.api.naverApiService
import com.chzzk.temtem.domain.User
import com.chzzk.temtem.utils.AppDataStore.Companion.dataStore
import com.chzzk.temtem.utils.PreferencesKeys
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NaverLogin(private val context: Context, val viewModel: MainViewModel) {

    fun naverLogin(context: Context, viewModel: MainViewModel) {
        val client = BuildConfig.Naver_Client_Key
        val secret = BuildConfig.Naver_Secret_Key
        val clientName = "점례동화"

        NaverIdLoginSDK.initialize(this.context, client, secret, clientName)

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                Log.d("abcdefr", "AccessToken : " + NaverIdLoginSDK.getAccessToken())
                Log.d("test", "client id : " + client)
                Log.d("test", "ReFreshToken : " + NaverIdLoginSDK.getRefreshToken())
                Log.d("test", "Expires : " + NaverIdLoginSDK.getExpiresAt().toString())
                Log.d("test", "TokenType : " + NaverIdLoginSDK.getTokenType())
                Log.d("test", "State : " + NaverIdLoginSDK.getState().toString())

                val accessToken = NaverIdLoginSDK.getAccessToken()

                runBlocking {
                    if (accessToken != null) {
                        saveToken(accessToken)
                        Log.d("abcdefr", "AccessToken2 : " + accessToken)
                    }
                }
                fetch_profile(accessToken)
                this@NaverLogin.viewModel.loginState(true)
                Log.d("loginState", "${this@NaverLogin.viewModel.isLogined.value}")

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

    private suspend fun saveToken(token: String?) {
        if (token != null) {
            Log.d("saveToken", "전달받은 AccessToken: $token")
            dataStore.edit { preferences: MutablePreferences ->
                preferences[PreferencesKeys.NAVER_ACCESS_TOKEN] = token
            }
            // 변경 사항이 적용된 후에 토큰을 다시 가져와서 로그를 출력
            val updatedToken = dataStore.data.first()[PreferencesKeys.NAVER_ACCESS_TOKEN]
            Log.d("saveToken", "저장된 AccessToken: $updatedToken")
        } else {
            Log.e("saveToken", "토큰이 null입니다.")
        }
    }

    private fun getToken(): String? = runBlocking {
        val preferences = dataStore.data.first()
        preferences[PreferencesKeys.NAVER_ACCESS_TOKEN]
    }

    fun fetch_profile(token: String?) {
        val accessToken = "Bearer $token"
        val call = naverApiService.getUserProfile(accessToken)
        call.enqueue(object : Callback<NaverUserProfileResponse> {
            override fun onResponse(call: Call<NaverUserProfileResponse>, response: Response<NaverUserProfileResponse>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()?.response
                    if (userProfile != null) {
                        Log.d("AutoLogin", "User userProfile: $userProfile")
                        Log.d("AutoLogin", "User ID: ${userProfile.id}")
                        Log.d("AutoLogin", "User Name: ${userProfile.name}")
                        Log.d("AutoLogin", "User Email: ${userProfile.email}")

                        val user = User(
                            email = userProfile.email,
                            name = userProfile.name,
                            id = userProfile.id
                        )
                        viewModel._userState.value = viewModel._userState.value.copy(data = user)
                    } else {
                        Log.e("AutoLogin", "Failed to get user profile: Response body is null")
                        this@NaverLogin.viewModel.loginState(false)
                    }
                } else {
                    Log.e("AutoLogin", "Failed to get user profile: ${response.errorBody()?.string()}")
                    this@NaverLogin.viewModel.loginState(false)
                }
            }

            override fun onFailure(call: Call<NaverUserProfileResponse>, t: Throwable) {
                Log.e("AutoLogin", "API call failed: ${t.message}")
            }
        })
    }

    fun logout(context: Context) {
        val clientId = BuildConfig.Naver_Client_Key
        val clientSecret = BuildConfig.Naver_Secret_Key
        val accessToken = getToken() ?: return

        val baseUrl = "https://nid.naver.com/oauth2.0/token"
        val grantType = "delete"

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
                    Log.d("Logout", "Successfully logged out")

                    runBlocking {
                        clearToken()
                    }

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

    private suspend fun clearToken() {
        dataStore.edit { preferences: MutablePreferences ->
            preferences.remove(PreferencesKeys.NAVER_ACCESS_TOKEN)
        }
    }
}
