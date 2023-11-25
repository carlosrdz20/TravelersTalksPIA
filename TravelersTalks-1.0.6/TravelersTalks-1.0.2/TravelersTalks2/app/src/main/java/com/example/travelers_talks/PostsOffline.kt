package com.example.travelers_talks

data class PostsOffline(
    val Title: String?,
    val description: String?= null,
    val idUser: Int?,
    val idCountry: Int?,
    val active: Int?=null,
    val id: Int?=null,
    val PostImg: List<ByteArray>? = null
)
