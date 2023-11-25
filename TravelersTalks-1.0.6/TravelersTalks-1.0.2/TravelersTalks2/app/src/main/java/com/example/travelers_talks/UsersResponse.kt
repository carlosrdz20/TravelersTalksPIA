package com.example.travelers_talks

data class UsersResponse(
    val idUser: Int,
    val nickname: String,
    val nameUser: String,
    val lastName: String,
    val email: String,
    val passwordUser: String,
    val imageURL: String
)
