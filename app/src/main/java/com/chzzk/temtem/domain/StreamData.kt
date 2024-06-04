package com.chzzk.temtem.domain

data class StreamDetail(
    val code : Int,
    val message : String,
    val content : DetailContent

)

data class StreamSimple(
    val code : Int,
    val message : String?,
    val content : SimpleContent
)