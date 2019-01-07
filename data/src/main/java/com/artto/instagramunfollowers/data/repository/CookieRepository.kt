package com.artto.instagramunfollowers.data.repository

import android.content.SharedPreferences
import com.artto.instagramunfollowers.data.api.Cookie

class CookieRepository(private val preferences: SharedPreferences) {

    fun getAll(): List<Cookie> = preferences.all
            .toList()
            .map {
                Cookie(it.first, it.second?.toString()
                        ?: "")
            }

    fun get(key: String): Cookie = Cookie(key, preferences.getString(key, "")
            ?: "")

    fun save(vararg cookies: Cookie) = preferences.edit()
            .apply { cookies.forEach { putString(it.key, it.value) } }
            .apply()

    fun remove(vararg keys: String) = preferences.edit()
            .apply { keys.forEach { remove(it) } }
            .apply()

    fun clear() = preferences.edit()
            .clear()
            .apply()

}