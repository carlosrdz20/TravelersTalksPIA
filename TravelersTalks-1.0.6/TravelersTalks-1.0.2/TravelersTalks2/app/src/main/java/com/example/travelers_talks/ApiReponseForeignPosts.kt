package com.example.travelers_talks

data class ApiReponseForeignPosts(
    val PostId: Int?=null,
    val TituloPost: String?=null,
    val DatePost: String?=null,
    val Description: String?=null,
    val IdCount: Int?=null,
    val IdUser: Int?=null,
    val Rating:Int?=null,
    val UserNickname: String?=null,
    val imageUser: String?=null,
    val CountryName: String?=null,
    val countryImg: String?=null,
    val ImagenUno: String?=null,
    val ImagenDos: String?=null,
    val ImagenTres: String?=null,
    val activeS: Int?=null
)
