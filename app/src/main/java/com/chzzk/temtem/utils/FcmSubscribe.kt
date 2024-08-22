package com.chzzk.temtem.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

fun subscribeTopics(topic: String) {
    FirebaseMessaging.getInstance().subscribeToTopic(topic)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // 구독 성공
                Log.d("FirebaseMessaging", "Subscribed to $topic")
                println(" $topic 구독 성공")
            } else {
                // 구독 실패
                println("$topic 구독 실패")
            }
        }
}

fun subscribeAllTopics() {
    val topics = listOf("broadcast_alert", "tem_alarm", "tem_twitter", "notice")
    for (topic in topics) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("Subscribed to $topic")
                }
            }
    }
}

fun unsubscribeTopic(topic: String) {
    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FirebaseMessaging", "UnSubscribed to $topic")
                println("$topic 구독 해제 성공")
            } else {
                println("$topic 구독 해제 실패")
            }
        }
}

fun unsubscribeAllTopics() {
    val topics = listOf("broadcast_alert", "tem_alarm", "tem_twitter", "notice")
    for (topic in topics) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("UnSubscribed to $topic")
                }
            }
    }
}