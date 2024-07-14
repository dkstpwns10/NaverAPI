package com.chzzk.temtem.utils

import androidx.datastore.preferences.core.stringPreferencesKey
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore


object PreferencesKeys {
    val NAVER_ACCESS_TOKEN = stringPreferencesKey("NAVER_ACCESS_TOKEN")
}
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "NAVER_ACCESS_TOKEN")