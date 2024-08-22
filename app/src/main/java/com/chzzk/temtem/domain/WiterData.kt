package com.chzzk.temtem.domain

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class WiterData(
    val num : Int,
    val name : String,
    val writer : String,
    val content : String,
    val a_date : String,
    val url : String
)
