package com.artto.instagramunfollowers.data

import android.content.SharedPreferences
import com.artto.instagramunfollowers.data.entity.User

class ApplicationUserDataStore(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val KEY_USER_ID = "userId"
        const val KEY_IS_AUTHORIZED = "isAuthorized"
        const val KEY_USER_NAME = "userName"
        const val KEY_FULL_NAME = "fullName"
        const val KEY_USER_PASS = "password"
        const val KEY_AVATAR_TMB_URL = "avatarTmbUrl"
        const val KEY_FOLLOWER_COUNT = "followerCount"
        const val KEY_FOLLOWING_COUNT = "followingCount"
    }

    fun load() = User().apply {
        with(sharedPreferences) {
            id = getString(KEY_USER_ID, "") ?: ""
            isAuthorized = getBoolean(KEY_IS_AUTHORIZED, false)
            userName = getString(KEY_USER_NAME, "") ?: ""
            fullName = getString(KEY_FULL_NAME, "") ?: ""
            password = getString(KEY_USER_PASS, "") ?: ""
            avatarThumbnailUrl = getString(KEY_AVATAR_TMB_URL, "") ?: ""
            followersCount = getInt(KEY_FOLLOWER_COUNT, 0)
            followingCount = getInt(KEY_FOLLOWING_COUNT, 0)
        }
    }


    fun save(user: User) = with(user) {
        sharedPreferences.edit()
                .putString(KEY_USER_ID, id)
                .putBoolean(KEY_IS_AUTHORIZED, isAuthorized)
                .putString(KEY_USER_NAME, userName)
                .putString(KEY_FULL_NAME, fullName)
                .putString(KEY_USER_PASS, password)
                .putString(KEY_AVATAR_TMB_URL, avatarThumbnailUrl)
                .putInt(KEY_FOLLOWER_COUNT, followersCount)
                .putInt(KEY_FOLLOWING_COUNT, followingCount)
                .apply()
    }


    fun clear() = sharedPreferences.edit().clear().apply()
}