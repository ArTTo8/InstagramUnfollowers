package com.artto.unfollowers.data.local.db.repository

import com.artto.unfollowers.data.local.db.dao.StatisticDao
import com.artto.unfollowers.data.local.db.entity.StatisticEntity
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class StatisticRepository(private val dao: StatisticDao) {

    fun getAll(): Single<List<StatisticEntity>> = dao.getAll()
            .subscribeOn(Schedulers.io())

    fun insert(recordEntity: StatisticEntity): Single<Long> = Single.fromCallable { dao.insert(recordEntity) }
            .subscribeOn(Schedulers.io())

    fun update(recordEntity: StatisticEntity): Single<Int> = Single.fromCallable { dao.update(recordEntity) }
            .subscribeOn(Schedulers.io())

    fun clear(): Completable = Completable.fromAction { dao.clear() }
            .subscribeOn(Schedulers.io())

}