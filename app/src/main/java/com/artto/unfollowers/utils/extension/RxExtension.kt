package com.artto.unfollowers.utils.extension

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

fun <T> Single<T>.withSchedulers(observeOn: Scheduler, subscribeOn: Scheduler): Single<T> =
        observeOn(observeOn).subscribeOn(subscribeOn)

fun <T> Single<T>.withProgress(progress: (Boolean) -> Unit): Single<T> =
        doOnSubscribe { progress.invoke(true) }.doOnEvent { _, _ -> progress.invoke(false) }

fun Completable.withSchedulers(observeOn: Scheduler, subscribeOn: Scheduler): Completable =
        observeOn(observeOn).subscribeOn(subscribeOn)

fun Completable.withProgress(progress: (Boolean) -> Unit): Completable =
        doOnSubscribe { progress.invoke(true) }.doOnComplete { progress.invoke(false) }