package com.example.travelers_talks

data class MyPosts(
    val NickName: String? = null,
    val pfpImg: ByteArray? = null,
    val Date: String?,
    val Title: String?,
    val Rating: Float,
    val RatingImg: String,
    val FlagImg: ByteArray? = null,
    val HeartImg: String,
    val PostImg: List<ByteArray>? = null,
    val Button: String,
    val idPost: Int?,
    val idCountry: Int?,
    val idUser: Int?,
    val description: String?,
    val arrowImg:String,
    val editImg:String,
    val deleteImg:String,
    val countryName: String?
)