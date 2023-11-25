package com.example.travelers_talks

data class PostsForeign(
    val NickName: String? = null,
    val pfpImg: ByteArray? = null,
    val Date: String?,
    val Title: String?,
    val Rating: Int?,
    val RatingImg: String,
    val FlagImg: ByteArray? = null,
    var HeartImg: String,
    var buttonF: String?=null,
    val PostImg: List<ByteArray>? = null,
    val idPost: Int?,
    val idCountry: Int?,
    val idUser: Int?,
    val description: String?
)
