package com.artto.instagramunfollowers.di

import android.content.Context
import com.artto.instagramunfollowers.data.InstagramRepository
import com.artto.instagramunfollowers.data.UserDataStore
import com.artto.instagramunfollowers.ui.login.LoginPresenter
import com.artto.instagramunfollowers.ui.main.MainPresenter
import com.artto.instagramunfollowers.utils.KEY_SHARED_USER
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


val uiModule = module {

    factory { MainPresenter(get()) }
    factory { LoginPresenter(get()) }

}

val dataModule = module {

    single { androidContext().getSharedPreferences(KEY_SHARED_USER, Context.MODE_PRIVATE) }
    single { UserDataStore(get()) }

    single { InstagramRepository(get()) }

}