package com.chzzk.temtem.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.chzzk.temtem.utils.AppDataStore.Companion.settingsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsRepo {

    companion object {
        private val FIRST_RUN_KEY = booleanPreferencesKey("first_run")
        private val broadcast_alert = booleanPreferencesKey("broadcast_alert")
        private val tem_alarm = booleanPreferencesKey("tem_alarm")
        private val tem_twitter = booleanPreferencesKey("tem_twitter")
        private val notice = booleanPreferencesKey("notice")
    }

    // 각 알림 설정의 상태를 가져오는 함수들
    val broadcastNotificationFlow: Flow<Boolean> = settingsDataStore.data.map { preferences ->
        preferences[broadcast_alert] ?: true
    }

    val teamNotificationFlow: Flow<Boolean> = settingsDataStore.data.map { preferences ->
        preferences[tem_alarm] ?: true
    }

    val teamitterNotificationFlow: Flow<Boolean> = settingsDataStore.data.map { preferences ->
        preferences[tem_twitter] ?: true
    }

    val noticeNotificationFlow: Flow<Boolean> = settingsDataStore.data.map { preferences ->
        preferences[notice] ?: true
    }

    private suspend fun isFirstRun(): Boolean {
        return settingsDataStore.data.first()[FIRST_RUN_KEY] ?: true
    }
    private suspend fun setFirstRun(value: Boolean) {
        settingsDataStore.edit { preferences ->
            preferences[FIRST_RUN_KEY] = value
        }
    }
    suspend fun initializeAllTopics() {
        if (isFirstRun()) {
            // Subscribe to all topics
            subscribeAllTopics()
            setFirstRun(false)
        }
    }
    // 각 알림 설정의 상태를 변경하는 함수들
    suspend fun setBroadcastNotification(enabled: Boolean) {
        settingsDataStore.edit { preferences ->
            preferences[broadcast_alert] = enabled
        }
    }

    suspend fun setTeamNotification(enabled: Boolean) {
        settingsDataStore.edit { preferences ->
            preferences[tem_alarm] = enabled
        }
    }

    suspend fun setTeamitterNotification(enabled: Boolean) {
        settingsDataStore.edit { preferences ->
            preferences[tem_twitter] = enabled
        }
    }

    suspend fun setNoticeNotification(enabled: Boolean) {
        settingsDataStore.edit { preferences ->
            preferences[notice] = enabled
        }
    }

    suspend fun getNotificationState(topic: String): Boolean {
        val topics = booleanPreferencesKey(topic)
        return settingsDataStore.data.first()[topics] ?: true
    }
}