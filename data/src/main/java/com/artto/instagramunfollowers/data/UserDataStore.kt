package com.artto.instagramunfollowers.data

import android.content.SharedPreferences

class UserDataStore(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
    }

    fun loadUserData(): Pair<String, String> = sharedPreferences.run {
        Pair(getString(KEY_USERNAME, ""), getString(KEY_PASSWORD, ""))
    }

    fun saveUserData(username: String, password: String) = sharedPreferences.edit()
            .putString(KEY_USERNAME, username)
            .putString(KEY_PASSWORD, password)
            .apply()

    fun clearUserData() = sharedPreferences.edit().clear().apply()

}