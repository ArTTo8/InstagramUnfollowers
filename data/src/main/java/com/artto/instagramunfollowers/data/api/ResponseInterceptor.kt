package com.artto.instagramunfollowers.data.api

import com.artto.instagramunfollowers.data.repository.CookieRepository
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor(private val cookiesStore: CookieRepository) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
            .apply { cookiesStore.save(*cookies.toTypedArray()) }


    private val Response.cookies: List<Cookie>
        get() = headers("Set-Cookie")
                .map { it.substringBefore(";", "") }
                .map {
                    val key = it.substringBefore("=", "")
                    val value = it.substringAfter("=", "")
                    Cookie(key, value)
                }
                .filter { it.key.isNotEmpty() }


}