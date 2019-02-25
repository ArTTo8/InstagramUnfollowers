package com.artto.instagramunfollowers.di

import android.content.Context
import androidx.room.Room
import com.artto.instagramunfollowers.data.local.UserDataStore
import com.artto.instagramunfollowers.data.local.db.AppDatabase
import com.artto.instagramunfollowers.data.local.db.repository.StatisticRepository
import com.artto.instagramunfollowers.data.remote.InstagramRepository
import com.artto.instagramunfollowers.ui.login.LoginPresenter
import com.artto.instagramunfollowers.ui.main.MainPresenter
import com.artto.instagramunfollowers.ui.menu.MenuPresenter
import com.artto.instagramunfollowers.ui.statistic.StatisticPresenter
import com.artto.instagramunfollowers.utils.KEY_SHARED_USER
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


val uiModule = module {

    factory { MainPresenter(get()) }
    factory { LoginPresenter(get()) }
    factory { MenuPresenter(get()) }
    factory { StatisticPresenter(get()) }

}

val dataModule = module {

    single { androidContext().getSharedPreferences(KEY_SHARED_USER, Context.MODE_PRIVATE) }
    single { UserDataStore(get()) }

    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "unfollowers").build() }
    single { (get() as AppDatabase).statisticDao() }
    single { StatisticRepository(get()) }

    single { InstagramRepository(get(), get()) }

}