package com.chattyapp.mychatapp

import android.app.Application
import com.chattyapp.timber.Timber

class MyChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}