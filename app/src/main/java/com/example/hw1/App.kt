package com.example.hw1

import android.app.Application
import com.example.hw1.utilities.SharedPreferencesManager
import com.example.hw1.utilities.SignalManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SignalManager.init(this)
        SharedPreferencesManager.init(this)
    }
}