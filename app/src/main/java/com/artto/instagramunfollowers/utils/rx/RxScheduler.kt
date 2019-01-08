package com.artto.instagramunfollowers.utils.rx

import io.reactivex.Scheduler
import io.reactivex.Single

fun <T> Single<T>.with(observeOn: Scheduler, subscribeOn: Scheduler): Single<T> =
        observeOn(observeOn).subscribeOn(subscribeOn)