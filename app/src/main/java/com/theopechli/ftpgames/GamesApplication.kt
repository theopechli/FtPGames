package com.theopechli.ftpgames

import android.app.Application
import com.theopechli.ftpgames.data.AppContainer
import com.theopechli.ftpgames.data.DefaultAppContainer

class GamesApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}