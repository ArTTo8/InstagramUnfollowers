package com.artto.unfollowers.utils.extension

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.withSchedulers(observeOn: Scheduler, subscribeOn: Scheduler): Observable<T> =
        observeOn(observeOn).subscribeOn(subscribeOn)

fun <T> Observable<T>.withProgress(progress: (Boolean) -> Unit): Observable<T> =
        doOnSubscribe { progress.invoke(true) }.doOnTerminate { progress.invoke(false) }

fun <T> Single<T>.withSchedulers(observeOn: Scheduler, subscribeOn: Scheduler): Single<T> =
        observeOn(observeOn).subscribeOn(subscribeOn)

fun <T> Single<T>.withProgress(progress: (Boolean) -> Unit): Single<T> =
        doOnSubscribe { progress.invoke(true) }.doOnEvent { _, _ -> progress.invoke(false) }

fun Completable.withSchedulers(observeOn: Scheduler, subscribeOn: Scheduler): Completable =
        observeOn(observeOn).subscribeOn(subscribeOn)

fun Completable.withProgress(progress: (Boolean) -> Unit): Completable =
        doOnSubscribe { progress.invoke(true) }.doOnComplete { progress.invoke(false) }