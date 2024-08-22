package com.chzzk.temtem.domain

import kotlinx.serialization.Serializable


@Serializable
data class StatusContent(
    val liveTitle: String,
    val status: String,
    val concurrentUserCount: Int,
    val accumulateCount: Int,
    val paidPromotion: Boolean,
    val adult: Boolean,
    val clipActive: Boolean,
    val chatChannelId: String,
    val tags: List<String>,
    val categoryType: String,
    val liveCategory: String,
    val liveCategoryValue: String,
    val livePollingStatusJson: String,
    val faultStatus: String?,
    val userAdultStatus: String?,
    val chatActive: Boolean,
    val chatAvailableGroup: String,
    val chatAvailableCondition: String,
    val minFollowerMinute: Int,
    val chatDonationRankingExposure: Boolean
)
@Serializable
data class DetailContent(
    val accumulateCount: Int,
    val adParameter: AdParameter,
    val adult: Boolean,
    val categoryType: String,
    val channel: Channel,
    val chatActive: Boolean,
    val chatAvailableCondition: String,
    val chatAvailableGroup: String,
    val chatChannelId: String,
    val chatDonationRankingExposure: Boolean,
    val clipActive: Boolean,
    val closeDate: String?,
    val concurrentUserCount: Int,
    val defaultThumbnailImageUrl: String?,
    val liveCategory: String,
    val liveCategoryValue: String,
    val liveId: Int,
    val liveImageUrl: String,
    val livePlaybackJson: String,
    val livePollingStatusJson: String,
    val liveTitle: String,
    val minFollowerMinute: Int,
    val openDate: String?,
    val p2pQuality: List<String>,
    val paidPromotion: Boolean,
    val status: String,
    val tags: List<String>,
    val userAdultStatus: String?
)
@Serializable
data class AdParameter(
    val tag: String
)
@Serializable
data class Channel(
    val channel: String,
    val channelName : String,
    val channelImageUrl : String,
    val verifiedMark : Boolean
)
@Serializable
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