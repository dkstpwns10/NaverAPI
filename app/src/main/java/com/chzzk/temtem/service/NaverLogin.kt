package com.chzzk.temtem.service

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.chzzk.temtem.BuildConfig
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse

class NaverLogin(private val context: Context) {
    val secretKey: String = BuildConfig.Naver_Secret_Key
    val clientKey: String = BuildConfig.Naver_Client_Key

    val oauthLoginCallBack: OAuthLoginCallback = object : OAuthLoginCallback {
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }

        override fun onFailure(httpStatus: Int, message: String) {
            onFailure(httpStatus, message)
        }

        override fun onSuccess() {
            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(result: NidProfileResponse) {
                    var name = result.profile?.name.toString()
                    var email = result.profile?.email.toString()
                    var gender = result.profile?.gender.toString()
                    Log.e(TAG, "네이버 로그인한 유저 정보 - 이름 : $name")
                    Log.e(TAG, "네이버 로그인한 유저 정보 - 이메일 : $email")
                    Log.e(TAG, "네이버 로그인한 유저 정보 - 성별 : $gender")
                }

                override fun onError(errorCode: Int, message: String) {
                    //
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    //
                }
            })
        }
    }

    fun authenticate() {
        NaverIdLoginSDK.initialize(context, BuildConfig.Naver_Client_Key,BuildConfig.Naver_Secret_Key, "temtem")
        NaverIdLoginSDK.authenticate(context, oauthLoginCallBack)
    }
}
