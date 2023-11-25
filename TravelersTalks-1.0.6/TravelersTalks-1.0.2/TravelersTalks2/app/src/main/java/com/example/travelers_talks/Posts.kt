package com.example.travelers_talks

data class Posts(
    val NickName: String? = null,
    val pfpImg: ByteArray? = null,
    val Date: String?,
    val Title: String?,
    val Rating: Int?,
    val RatingImg: String,
    val FlagImg: ByteArray? = null,
    var HeartImg: String,
    val PostImg: List<ByteArray>? = null,
    var Button: String,
    val idPost: Int?,
    val idCountry: Int?,
    val idUser: Int?,
    val description: String?
)
