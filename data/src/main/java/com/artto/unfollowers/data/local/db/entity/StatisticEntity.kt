package com.artto.unfollowers.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artto.unfollowers.data.local.db.DBConfig
import java.util.*

@Entity(tableName = DBConfig.Statistic.TABLE_NAME)
data class StatisticEntity(

        @PrimaryKey()
        @ColumnInfo(name = DBConfig.Statistic.DATE)
        val date: Date,

        @ColumnInfo(name = DBConfig.Statistic.FOLLOWERS_COUNT)
        val followersCount: Int,

        @ColumnInfo(name = DBConfig.Statistic.FOLLOWING_COUNT)
        val followingCount: Int,

        @ColumnInfo(name = DBConfig.Statistic.UNFOLLOWERS_COUNT)
        val unfollowersCount: Int

)