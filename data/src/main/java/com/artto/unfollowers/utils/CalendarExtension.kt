package com.artto.unfollowers.utils

import java.util.*

fun Calendar.setTimeToDateStart(): Calendar =
        this.apply {
//            set(Calendar.HOUR_OF_DAY, 0)
//            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }