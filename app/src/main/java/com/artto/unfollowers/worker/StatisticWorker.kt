package com.artto.unfollowers.worker

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.artto.unfollowers.data.remote.InstagramRepository
import com.artto.unfollowers.notification.NotificationDispatcher
import io.reactivex.Single
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class StatisticWorker(appContext: Context, workerParams: WorkerParameters) : RxWorker(appContext, workerParams), KoinComponent {

    private val notificationDispatcher: NotificationDispatcher by inject()
    private val instagramRepository: InstagramRepository by inject()

    override fun createWork(): Single<Result> =
            with(instagramRepository.getUserData()) {
                if (first.isNotBlank() && second.isNotBlank())
                    instagramRepository.logIn(first, second)
                            .flatMap { instagramRepository.getUnfollowers() }
                            .doOnSuccess { notificationDispatcher.sendNotification("Statistic saved", "Unfollowers count - ${it.size}") }
                            .doOnError { notificationDispatcher.sendNotification("Statistic saving error", it.localizedMessage) }
                            .map { Result.success() }
                            .onErrorReturn { Result.failure() }
                else
                    Single.just(Result.failure())
            }
}