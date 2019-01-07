package com.artto.instagramunfollowers

import android.app.Application
import com.artto.instagramunfollowers.di.uiModule
import com.artto.instagramunfollowers.di.dataModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(uiModule, dataModule))
    }

}