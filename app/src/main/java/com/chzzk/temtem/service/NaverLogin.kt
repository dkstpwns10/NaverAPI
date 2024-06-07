package com.chzzk.temtem.service

import android.content.Context

import com.chzzk.temtem.api.ApiService
import com.chzzk.temtem.domain.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class NaverLogin(private val context: Context) {
    private lateinit var service: ApiService
    fun naver(){
        val retrofit = Retrofit.Builder()
            .baseUrl("YOUR_SERVER_URL")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        val call: Call<User>? = service.loginWithNaver()
        call?.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val loginResponse: User? = response.body()
                    // 필요한 작업 수행
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }
    }
//    val secretKey: String = BuildConfig.Naver_Secret_Key
//    val clientKey: String = BuildConfig.Naver_Client_Key
//
//    val oauthLoginCallBack: OAuthLoginCallback = object : OAuthLoginCallback {
//        override fun onError(errorCode: Int, message: String) {
//            onFailure(errorCode, message)
//        }
//
//        override fun onFailure(httpStatus: Int, message: String) {
//            onFailure(httpStatus, message)
//        }
//
//        override fun onSuccess() {
//            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
//                override fun onSuccess(result: NidProfileResponse) {
//                    var name = result.profile?.name.toString()
//                    var email = result.profile?.email.toString()
//                    var gender = result.profile?.gender.toString()
//                    Log.e(TAG, "네이버 로그인한 유저 정보 - 이름 : $name")
//                    Log.e(TAG, "네이버 로그인한 유저 정보 - 이메일 : $email")
//                    Log.e(TAG, "네이버 로그인한 유저 정보 - 성별 : $gender")
//                }
//
//                override fun onError(errorCode: Int, message: String) {
//                    //
//                }
//
//                override fun onFailure(httpStatus: Int, message: String) {
//                    //
//                }
//            })
//        }
//    }
//
//    fun authenticate() {
//        NaverIdLoginSDK.initialize(context, BuildConfig.Naver_Client_Key,BuildConfig.Naver_Secret_Key, "temtem")
//        NaverIdLoginSDK.authenticate(context, oauthLoginCallBack)
//    }

