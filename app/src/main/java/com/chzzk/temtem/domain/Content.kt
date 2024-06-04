package com.chzzk.temtem.domain

data class DetailContent(
    val liveId : Int,
    val liveTitle : String,
    val status : String,
    val liveImageUrl : String,
    val defaultThumbnailImageUrl : String?,
    val concurrentUserCount : Int,
    val accumulateCount : Int,
    val openDate : String?,
    val closeDate : String?,
    val adult : Boolean,
    val clipActive : Boolean,
    val tags : List<String>,
    val categoryType : String,
    val liveCategory : String,
    val liveCategoryValue : String,
    val chatActive : Boolean,
    val chatAvailableGroup : String,
    val paidPromotion : Boolean,
    val chatAvailableCondition : String,
    val minFollowerMinute : Int,
    val livePlaybackJson : Map<String,String>,
    val p2pQuality : List<String>,
    val channel : Channel,
    val chatDonationRankingExposure : Boolean
)

data class Channel(
    val channel: String,
    val channelName : String,
    val channelImageUrl : String,
    val verifiedMark : Boolean
)

data class SimpleContent(
    val channelId : String,
    val channelName : String,
    val channelImageUrl : String,
    val verifiedMark : Boolean,
    val channelType : String,
    val channelDescription : String?,
    val followerCount : Int,
    val openLive : Boolean,
    val subscriptionAvailability : Boolean
)