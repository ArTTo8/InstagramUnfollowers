package com.artto.unfollowers.di

import android.content.Context
import androidx.room.Room
import com.artto.unfollowers.analytics.AnalyticsManager
import com.artto.unfollowers.data.local.UserDataStore
import com.artto.unfollowers.data.local.db.AppDatabase
import com.artto.unfollowers.data.local.db.repository.StatisticRepository
import com.artto.unfollowers.data.repository.InstagramRepository
import com.artto.unfollowers.data.repository.UserRepository
import com.artto.unfollowers.notifications.NotificationsManager
import com.artto.unfollowers.ui.login.LoginPresenter
import com.artto.unfollowers.ui.main.MainPresenter
import com.artto.unfollowers.ui.menu.MenuPresenter
import com.artto.unfollowers.ui.rate.RatePresenter
import com.artto.unfollowers.ui.statistic.StatisticPresenter
import com.artto.unfollowers.utils.KEY_SHARED_USER
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


val uiModule = module {

    factory { MainPresenter(get(), get(), get()) }
    factory { LoginPresenter(get(), get(), get()) }
    factory { MenuPresenter(get(), get()) }
    factory { StatisticPresenter(get(), get()) }
    factory { RatePresenter(get(), get()) }

}

val dataModule = module {

    single { AnalyticsManager(androidContext()) }
    single { NotificationsManager(androidContext()) }

    single { androidContext().getSharedPreferences(KEY_SHARED_USER, Context.MODE_PRIVATE) }
    single { UserDataStore(get()) }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "unfollowers")
                .fallbackToDestructiveMigration()
                .build()
    }

    single { (get() as AppDatabase).statisticDao() }

    single { StatisticRepository(get()) }
    single { InstagramRepository(get(), get()) }
    single { UserRepository(get()) }

}