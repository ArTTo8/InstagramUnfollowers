package com.artto.unfollowers

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.appodeal.ads.Appodeal
import com.artto.unfollowers.di.dataModule
import com.artto.unfollowers.di.uiModule
import com.facebook.stetho.Stetho
import com.google.android.gms.ads.MobileAds
import org.koin.android.ext.android.startKoin

class App : MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG)
            initDebug()

        startKoin(this, listOf(uiModule, dataModule))
    }

    private fun initDebug() {
        Stetho.initializeWithDefaults(this)
    }

}