package com.artto.unfollowers.data.local.db

import androidx.room.TypeConverter
import java.util.*

class TimeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(value: Date?): Long? = value?.time

}