package com.artto.unfollowers

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.artto.unfollowers.di.dataModule
import com.artto.unfollowers.di.uiModule
import org.koin.android.ext.android.startKoin

class App : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(uiModule, dataModule))
    }

}