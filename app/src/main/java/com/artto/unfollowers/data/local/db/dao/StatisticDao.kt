package com.artto.unfollowers.data.local.db.dao

import androidx.room.*
import com.artto.unfollowers.data.local.db.DBConfig
import com.artto.unfollowers.data.local.db.entity.StatisticEntity
import io.reactivex.Single

@Dao
interface StatisticDao {

    @Query("SELECT * FROM ${DBConfig.Statistic.TABLE_NAME} WHERE ${DBConfig.Statistic.USER_ID} = :userId")
    fun getAll(userId: Long): Single<List<StatisticEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(statisticsRecordEntity: StatisticEntity): Long

    @Update
    fun update(statisticsRecordEntity: StatisticEntity): Int

    @Query("DELETE FROM ${DBConfig.Statistic.TABLE_NAME}")
    fun clear()

}