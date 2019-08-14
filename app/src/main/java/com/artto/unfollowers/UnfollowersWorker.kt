package com.artto.unfollowers

import android.content.Context
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import com.artto.unfollowers.data.local.db.repository.StatisticRepository
import com.artto.unfollowers.data.repository.InstagramRepository
import com.artto.unfollowers.data.repository.UserRepository
import com.artto.unfollowers.notifications.NotificationsManager
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.lang.Exception

class UnfollowersWorker(context: Context, parameters: WorkerParameters) : RxWorker(context, parameters), KoinComponent {

    private val userRepository: UserRepository by inject()
    private val instagramRepository: InstagramRepository by inject()
    private val statisticRepository: StatisticRepository by inject()
    private val notificationsManager: NotificationsManager by inject()

    override fun createWork(): Single<Result> {
        val user = userRepository.getUserData()
        return if (user.username.isBlank() || user.password.isBlank())
            Single.just(Result.failure())
        else
            instagramRepository.logIn(user.username, user.password)
                    .flatMap { getOldUnfollowersCount() }
                    .zipWith(
                            getNewUnfollowersCount(),
                            BiFunction { old, new ->
                                if (old < new)
                                    notificationsManager.showNewUnfollowersNotification(new - old)
                                else
                                    notificationsManager.showNewUnfollowersNotification(-999)

                                return@BiFunction Result.success()
                            })
    }

    private fun getOldUnfollowersCount() =
            statisticRepository.getAll(instagramRepository.getUserId())
                    .map { it.lastOrNull()?.unfollowersCount ?: throw Exception() }

    private fun getNewUnfollowersCount() =
            instagramRepository.getUnfollowers()
                    .map { instagramRepository.users.filter { it.isFollowedByUser && !it.isFollower }.count() }

}