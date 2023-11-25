package com.example.travelers_talks

data class MyDrafts(
    val NickName: String? = null,
    val pfpImg: ByteArray? = null,
    val Date: String? = null,
    val Title: String? = null,
    val Rating: Float? = null,
    val RatingImg: String? = null,
    val FlagImg: ByteArray? = null,
    val HeartImg: String? = null,
    val PostImg: List<ByteArray>? = null,
    val Button: String? = null,
    val idPost: Int? = null,
    val idCountry: Int? = null,
    val idUser: Int? = null,
    val description: String? = null,
    val arrowimg: String?=null,
    val editarBttn: String?=null,
    val borrarBttn: String?=null,
    val sendBttn: String?=null,
    val countryName: String? = null
)