package com.chzzk.temtem.utils

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class AppDataStore : Application() {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "NAVER_ACCESS_TOKEN")
        private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        lateinit var instance: AppDataStore
            private set
        val settingsDataStore: DataStore<Preferences>
            get() = instance.settingsDataStore
        val dataStore: DataStore<Preferences>
            get() = instance.dataStore
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
