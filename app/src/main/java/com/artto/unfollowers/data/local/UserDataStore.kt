package com.artto.unfollowers.data.local

import android.content.SharedPreferences
import java.util.*

class UserDataStore(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_FIRST_OPEN_DATE = "first_open"
        private const val KEY_RATE_DIALOG_SHOWED = "rate_dialog_showed"
    }

    fun loadUserData(): UserEntity = sharedPreferences.run {
        UserEntity(
                getString(KEY_USERNAME, "")
                        ?: "",
                getString(KEY_PASSWORD, "")
                        ?: "",
                Date(getLong(KEY_FIRST_OPEN_DATE, 0)),
                getBoolean(KEY_RATE_DIALOG_SHOWED, false)
        )
    }

    fun saveUserData(user: UserEntity) = sharedPreferences.edit()
            .putString(KEY_USERNAME, user.username)
            .putString(KEY_PASSWORD, user.password)
            .putLong(KEY_FIRST_OPEN_DATE, user.firstOpen.time)
            .putBoolean(KEY_RATE_DIALOG_SHOWED, user.rateDialogShowed)
            .apply()

    fun updateUserData(username: String? = null,
                       password: String? = null,
                       firstOpen: Date? = null,
                       rateDialogShowed: Boolean? = null) {
        saveUserData(loadUserData().also { loaded ->
            username?.let { loaded.username = it }
            password?.let { loaded.password = it }
            firstOpen?.let { if (loaded.firstOpen.time == 0L) loaded.firstOpen = it }
            rateDialogShowed?.let { loaded.rateDialogShowed = it }
        })
    }

}