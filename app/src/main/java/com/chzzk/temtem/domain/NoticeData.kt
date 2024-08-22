package com.chzzk.temtem.domain

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class NoticeData(
    val num: Int,
    val name: String,
    val content: String,
    val date: String
)
