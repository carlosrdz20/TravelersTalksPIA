package com.example.travelers_talks.data

data class ApiResponsePostsData(
    val idPost:Int,
    val titlePost: String,
    val idCountry:Int,
    val idUser:Int,
    val nickname: String,
    val imageURL: String
)
