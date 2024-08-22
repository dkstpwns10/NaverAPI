package com.chzzk.temtem.utils

import androidx.datastore.preferences.core.stringPreferencesKey
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val NAVER_ACCESS_TOKEN = stringPreferencesKey("NAVER_ACCESS_TOKEN")
}

fun PreferencesKeys.getNaverAccessToken() = NAVER_ACCESS_TOKEN

