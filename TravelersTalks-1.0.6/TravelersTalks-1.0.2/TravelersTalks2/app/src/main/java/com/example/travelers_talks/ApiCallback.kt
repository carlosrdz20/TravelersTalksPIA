package com.example.travelers_talks

interface ApiCallback {
    fun onApiSuccess()
    fun onApiFailure(error: String)
}