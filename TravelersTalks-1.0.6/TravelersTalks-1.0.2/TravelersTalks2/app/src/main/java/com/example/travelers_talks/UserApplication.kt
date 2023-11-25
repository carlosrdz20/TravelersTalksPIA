package com.example.travelers_talks

import android.app.Application
import com.example.travelers_talks.data.DataDbHelper

class UserApplication:Application() {
    companion object{
        lateinit var dbHelper: DataDbHelper
    }

    override fun onCreate() {
        super.onCreate()
        dbHelper =  DataDbHelper(applicationContext)
    }

}