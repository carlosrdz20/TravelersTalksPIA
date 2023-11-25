package com.example.travelers_talks

data class ApiResponseUserForeign(
    val idUser: Int,
    val nickname: String,
    val nameUser: String,
    val lastName: String,
    val imageURL: String,
    val followerCount: Int,
    val followingCount: Int,
    val activeF: Int?=null
)
