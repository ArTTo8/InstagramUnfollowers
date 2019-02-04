package com.artto.instagramunfollowers.data

import dev.niekirk.com.instagram4android.Instagram4Android
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

class InstagramRepository(private val userDataStore: UserDataStore) {

    private lateinit var instagram: Instagram4Android

    val users = ArrayList<InstagramUser>()

    fun getUserData() = userDataStore.loadUserData()


    fun logIn(username: String, password: String): Single<InstagramLoggedUser> = Single.create {
        instagram = Instagram4Android(username, password)
        instagram.setup()

        val result = instagram.login()
        userDataStore.saveUserData(instagram.username, instagram.password)
        it.onSuccess(result.logged_in_user)
    }


    fun logOut() {
        users.clear()
        userDataStore.clearUserData()
    }


    fun unfollow(userId: Long): Completable = Completable.create {
        instagram.sendRequest(InstagramUnfollowRequest(userId))
        users.apply { first { it.pk == userId }.isFollowedByUser = false }
        it.onComplete()
    }


    fun follow(userId: Long): Single<StatusResult> = Single.create {
        val result = instagram.sendRequest(InstagramFollowRequest(userId))
        users.apply { first { it.pk == userId }.isFollowedByUser = true }
        it.onSuccess(result)
    }


    fun getUnfollowers(): Single<List<InstagramUser>> = Single
            .zip(
                    getAllFollowers(),
                    getAllFollowing(),
                    BiFunction { followers: List<InstagramUserSummary>, following: List<InstagramUserSummary> ->
                        val temp = ArrayList(followers).apply { addAll(following) }
                        users.clear()
                        users.addAll(temp.map { InstagramUser(it, followers.contains(it), following.contains(it)) }.distinct())

                        users.filter { it.isFollowedByUser && !it.isFollower }
                    })


    private fun getAllFollowers(): Single<List<InstagramUserSummary>> = Single.create {
        val result = ArrayList<InstagramUserSummary>()
        var cursor = ""
        var hasNext = true

        while (hasNext) {
            val response = instagram.sendRequest(InstagramGetUserFollowersRequest(instagram.userId, cursor))
            result.addAll(response.users)

            if (response.next_max_id.isNullOrBlank())
                hasNext = false
            else
                cursor = response.next_max_id
        }

        it.onSuccess(result)
    }


    private fun getAllFollowing(): Single<List<InstagramUserSummary>> = Single.create {
        val result = ArrayList<InstagramUserSummary>()
        var cursor = ""
        var hasNext = true

        while (hasNext) {
            val response = instagram.sendRequest(InstagramGetUserFollowingRequest(instagram.userId, cursor))
            result.addAll(response.users)

            if (response.next_max_id.isNullOrBlank())
                hasNext = false
            else
                cursor = response.next_max_id
        }

        it.onSuccess(result)
    }

}