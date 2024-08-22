package com.chzzk.temtem.domain

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class AlarmData(
    val num : Int,
    val name : String,
    val writer : String,
    val stream : String,
    val a_date : String,
    val url : String
)
