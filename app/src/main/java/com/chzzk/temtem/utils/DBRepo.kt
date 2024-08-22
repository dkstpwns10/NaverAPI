package com.chzzk.temtem.utils

import com.chzzk.temtem.api.RetrofitDB
import com.chzzk.temtem.domain.AlarmData
import com.chzzk.temtem.domain.NoticeData
import com.chzzk.temtem.domain.WiterData
import retrofit2.await

class DBRepo {
    suspend fun getAlarmData(pageNumber: Int): List<AlarmData> {
        return RetrofitDB.api.getAlarmData(pageNumber)
    }


    suspend fun getWiterData(pageNumber: Int): List<WiterData> {
        return RetrofitDB.api.getWiterData(pageNumber)
    }


    suspend fun getNoticeData(pageNumber: Int): List<NoticeData> {
        return RetrofitDB.api.getNoticeData(pageNumber)
    }

}