package com.artto.instagramunfollowers.di

import android.content.Context
import android.webkit.WebView
import com.artto.instagramunfollowers.data.ApplicationUserDataStore
import com.artto.instagramunfollowers.data.api.ApiConstants
import com.artto.instagramunfollowers.data.api.ApiMethods
import com.artto.instagramunfollowers.data.api.RequestInterceptor
import com.artto.instagramunfollowers.data.api.ResponseInterceptor
import com.artto.instagramunfollowers.data.interactor.InstagramInteractor
import com.artto.instagramunfollowers.data.repository.ApplicationUserRepository
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

    factory { MainPresenter(get()) }
    factory { LoginPresenter(get()) }

}


val dataModule = module {

    single { CookieRepository(androidContext().getSharedPreferences("Cookies", Context.MODE_PRIVATE)) }
    single { createOkHttpClient(get(), WebView(androidContext()).settings.userAgentString) }
    single { ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) }
    single { createService<ApiMethods>(get(), get()) }
    single { InstagramRepository(get(), get()) }

    single { ApplicationUserDataStore(androidContext().getSharedPreferences("UserData", Context.MODE_PRIVATE)) }
    single { ApplicationUserRepository(get()) }

    single { InstagramInteractor(get(), get(), get()) }

}

fun createOkHttpClient(cookieRepository: CookieRepository, userAgentString: String): OkHttpClient =
        OkHttpClient.Builder()
                .addInterceptor(RequestInterceptor(cookieRepository, userAgentString))
                .addInterceptor(ResponseInterceptor(cookieRepository))
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()

inline fun <reified T> createService(okHttpClient: OkHttpClient, objectMapper: ObjectMapper): T =
        Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(T::class.java)