package com.chzzk.temtem.utils

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class AppDataStore : Application() {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "naver_access_token")

        lateinit var instance: AppDataStore
            private set

        val dataStore: DataStore<Preferences>
            get() = instance.dataStore
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
