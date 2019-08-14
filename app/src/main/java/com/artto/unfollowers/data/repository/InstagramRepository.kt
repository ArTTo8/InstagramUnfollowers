package com.artto.unfollowers.data.repository

import com.artto.unfollowers.data.local.db.entity.StatisticEntity
import com.artto.unfollowers.data.local.db.repository.StatisticRepository
import com.artto.unfollowers.data.remote.Instagram
import com.artto.unfollowers.data.remote.InstagramUser
import com.artto.unfollowers.utils.setTimeToDateStart
import dev.niekirk.com.instagram4android.requests.InstagramFollowRequest
import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowersRequest
import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowingRequest
import dev.niekirk.com.instagram4android.requests.InstagramUnfollowRequest
import dev.niekirk.com.instagram4android.requests.payload.InstagramLoggedUser
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary
import dev.niekirk.com.instagram4android.requests.payload.StatusResult
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.*

class InstagramRepository(private val statisticRepository: StatisticRepository,
                          private val userRepository: UserRepository) {

    private lateinit var instagram: Instagram

    val users = ArrayList<InstagramUser>()
    var userPhotoUrl = ""

    fun getUserId() = instagram.userId


    fun logIn(username: String, password: String): Single<InstagramLoggedUser> = Single.create {
        instagram = Instagram(username, password)
        instagram.setup()

        try {
            val result = instagram.login()
            userRepository.updateUserData(username = instagram.username, password = instagram.password, firstOpen = Date())
            result.logged_in_user?.let { loggedUser ->
                userPhotoUrl = loggedUser.profile_pic_url
                it.onSuccess(result.logged_in_user)
            } ?: it.onError(java.lang.Exception("Login error"))
        } catch (e: Exception) {
            it.tryOnError(e)
        }
    }


    fun logOut() {
        users.clear()
        userRepository.updateUserData(username = "", password = "")
    }


    fun unfollow(userId: Long): Completable = Completable.create {
        try {
            instagram.sendRequest(InstagramUnfollowRequest(userId))
            users.apply { first { it.pk == userId }.isFollowedByUser = false }
            it.onComplete()
        } catch (e: Exception) {
            it.tryOnError(e)
        }
    }


    fun follow(userId: Long): Single<StatusResult> = Single.create {
        try {
            val result = instagram.sendRequest(InstagramFollowRequest(userId))
            users.apply { first { it.pk == userId }.isFollowedByUser = true }
            it.onSuccess(result)
        } catch (e: Exception) {
            it.tryOnError(e)
        }
    }


    fun getUnfollowers(): Single<Long> = Single
            .zip(
                    getAllFollowers(),
                    getAllFollowing(),
                    BiFunction { followers: List<InstagramUserSummary>, following: List<InstagramUserSummary> ->
                        val temp = ArrayList(followers).apply { addAll(following) }
                        users.clear()
                        users.addAll(temp.map { InstagramUser(it, followers.contains(it), following.contains(it)) }.distinct())

                        return@BiFunction users.filter { it.isFollowedByUser && !it.isFollower }
                    })
            .flatMap { saveStatistic() }

    fun saveStatistic() = statisticRepository.insert(
            StatisticEntity(
                    Calendar.getInstance().setTimeToDateStart().time,
                    instagram.userId,
                    users.filter { it.isFollower }.size,
                    users.filter { it.isFollowedByUser }.size,
                    users.filter { it.isFollowedByUser && !it.isFollower }.size))

    private fun getAllFollowers(): Single<List<InstagramUserSummary>> = Single.create {
        val result = ArrayList<InstagramUserSummary>()
        var cursor = ""
        var hasNext = true

        try {
            while (hasNext) {
                val response = instagram.sendRequest(InstagramGetUserFollowersRequest(instagram.userId, cursor))
                result.addAll(response.users)

                if (response.next_max_id.isNullOrBlank())
                    hasNext = false
                else
                    cursor = response.next_max_id
            }

            it.onSuccess(result)
        } catch (e: Exception) {
            it.tryOnError(e)
        }
    }


    private fun getAllFollowing(): Single<List<InstagramUserSummary>> = Single.create {
        val result = ArrayList<InstagramUserSummary>()
        var cursor = ""
        var hasNext = true

        try {
            while (hasNext) {
                val response = instagram.sendRequest(InstagramGetUserFollowingRequest(instagram.userId, cursor))
                result.addAll(response.users)

                if (response.next_max_id.isNullOrBlank())
                    hasNext = false
                else
                    cursor = response.next_max_id
            }

            it.onSuccess(result)
        } catch (e: Exception) {
            it.tryOnError(e)
        }
    }


}