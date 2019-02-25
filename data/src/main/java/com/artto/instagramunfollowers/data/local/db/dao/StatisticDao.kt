package com.artto.instagramunfollowers.data.local.db.dao

import androidx.room.*
import com.artto.instagramunfollowers.data.local.db.DBConfig
import com.artto.instagramunfollowers.data.local.db.entity.StatisticEntity
import io.reactivex.Single

@Dao
interface StatisticDao {

    @Query("SELECT * FROM ${DBConfig.Statistic.TABLE_NAME}")
    fun getAll(): Single<List<StatisticEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(statisticsRecordEntity: StatisticEntity): Long

    @Update
    fun update(statisticsRecordEntity: StatisticEntity): Int

    @Query("DELETE FROM ${DBConfig.Statistic.TABLE_NAME}")
    fun clear()

}