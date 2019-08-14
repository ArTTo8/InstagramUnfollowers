package com.artto.unfollowers.data.repository

import com.artto.unfollowers.data.local.UserDataStore
import java.util.*

class UserRepository(private val dataStore: UserDataStore) {

    private var adShowCounter = 0

    fun needToShowRateDialog() = dataStore.loadUserData().let {
        if (it.rateDialogShowed)
            false
        else
            daysBetween(Date(), it.firstOpen) >= 2
    }

    fun needToShowAd(): Boolean =
            if (adShowCounter <= 0) {
                adShowCounter = 5
                true
            } else {
                --adShowCounter
                false
            }

    fun getUserData() = dataStore.loadUserData()

    fun updateUserData(username: String? = null,
                       password: String? = null,
                       firstOpen: Date? = null,
                       rateDialogShowed: Boolean? = null) = dataStore.updateUserData(username, password, firstOpen, rateDialogShowed)

    fun daysBetween(startDate: Date, endDate: Date) = (startDate.time / (24 * 60 * 60 * 1000)) - (endDate.time / (24 * 60 * 60 * 1000))

}