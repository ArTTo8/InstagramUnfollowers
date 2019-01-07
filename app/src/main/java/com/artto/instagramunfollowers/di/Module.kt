package com.artto.instagramunfollowers.di

import android.content.Context
import android.webkit.WebView
import com.artto.instagramunfollowers.data.api.ApiConstants
import com.artto.instagramunfollowers.data.api.ApiMethods
import com.artto.instagramunfollowers.data.api.RequestInterceptor
import com.artto.instagramunfollowers.data.api.ResponseInterceptor
import com.artto.instagramunfollowers.data.interactor.InstagramInteractor
import com.artto.instagramunfollowers.data.repository.CookieRepository
import com.artto.instagramunfollowers.data.repository.InstagramRepository
import com.artto.instagramunfollowers.ui.login.LoginPresenter
import com.artto.instagramunfollowers.ui.main.MainPresenter
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory


val uiModule = module {
    factory { MainPresenter() }
    factory { LoginPresenter(get()) }
}


val dataModule = module {
    single { CookieRepository(androidContext().getSharedPreferences("Cookies", Context.MODE_PRIVATE)) }

    single {
        OkHttpClient.Builder()
                .addInterceptor(RequestInterceptor(get(), WebView(androidContext()).settings.userAgentString))
                .addInterceptor(ResponseInterceptor(get()))
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()
    }

    single { ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) }

    single {
        Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(get())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(JacksonConverterFactory.create(get()))
                .build()
                .create<ApiMethods>(ApiMethods::class.java)
    }

    single { InstagramRepository(get()) }

    single { InstagramInteractor(get()) }
}