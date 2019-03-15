package com.artto.unfollowers.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsManager(private val context: Context) {

    enum class Event(val title: String) {
        FOLLOW("follow"),
        UNFOLLOW("unfollow"),
        LOGIN("login"),
        LOGOUT("logout"),
        STATISTIC_OPEN("statistic_open")
    }

    fun logEvent(event: Event, bundle: Bundle? = null) {
        FirebaseAnalytics.getInstance(context).logEvent(event.title, bundle)
    }

}