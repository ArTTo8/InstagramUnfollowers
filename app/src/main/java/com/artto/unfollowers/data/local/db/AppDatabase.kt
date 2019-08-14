package com.artto.unfollowers.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.artto.unfollowers.data.local.db.entity.StatisticEntity
import com.artto.unfollowers.data.local.db.AppDatabase.Companion.DATABASE_VERSION
import com.artto.unfollowers.data.local.db.dao.StatisticDao

@Database(entities = [StatisticEntity::class], version = DATABASE_VERSION)
@TypeConverters(TimeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun statisticDao(): StatisticDao

    companion object {
        const val DATABASE_VERSION = 1
    }

}