package com.prembros.facilis

import android.app.Application
import android.content.Context

class SampleApp : Application() {

    companion object {
        lateinit var INSTANCE: SampleApp
        fun appContext(): Context = INSTANCE.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}