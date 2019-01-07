package com.artto.instagramunfollowers.data.api

import com.artto.instagramunfollowers.data.repository.CookieRepository
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor(private val cookies: CookieRepository, private val userAgent: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.request().newBuilder()
            .apply {
                addHeader("user-agent", userAgent)
                addHeader(ApiConstants.Headers.INST_AJAX, "1")

                cookies.get(ApiConstants.Cookies.CRSF_TOKEN)
                        .takeIf { it.value.isNotBlank() }
                        ?.also { addHeader(ApiConstants.Headers.CRSF_TOKEN, it.value) }

                cookies.getAll().forEach {
                    if (it.value.isNotEmpty() && it.value != "\"\"") {
                        addHeader("Cookie", "${it.key}=${it.value};")
                    }
                }

            }
            .let { chain.proceed(it.build()) }

}