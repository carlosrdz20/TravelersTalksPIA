package com.example.travelers_talks

data class ApiResponseFavorites(
    val PostId: Int?=null,
    val titlePost: String?=null,
    val descPost: String?=null,
    val IdUsuarioPost: Int?=null,
    val usernickname: String?=null,
    val imageUser: String?=null,
    val CountryId: Int?=null,
    val countryImg: String?=null,
    val RowDate: String?=null,
    val Rating: Int?=null,
    val imageUno: String?=null,
    val imageDos: String?=null,
    val imageTres: String?=null
)
