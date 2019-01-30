package com.artto.instagramunfollowers.data

import dev.niekirk.com.instagram4android.Instagram4Android
import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowersRequest
import dev.niekirk.com.instagram4android.requests.InstagramGetUserFollowingRequest
import dev.niekirk.com.instagram4android.requests.InstagramUnfollowRequest
import dev.niekirk.com.instagram4android.requests.payload.InstagramLoggedUser
import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

class InstagramRepository(private val userDataStore: UserDataStore) {

    private lateinit var instagram: Instagram4Android

    val user = BehaviorSubject.create<InstagramLoggedUser>()

    fun getUserData() = userDataStore.loadUserData()

    fun login(username: String, password: String): Single<InstagramLoggedUser> = Single.create {
        instagram = Instagram4Android(username, password)
        instagram.setup()

        val result = instagram.login()
        userDataStore.saveUserData(instagram.username, instagram.password)
        user.onNext(result.logged_in_user)
        it.onSuccess(result.logged_in_user)
    }

    fun getFollowers(cursor: String): Single<Pair<String?, List<InstagramUserSummary>>> = Single.create {
        val result = instagram.sendRequest(InstagramGetUserFollowersRequest(instagram.userId, cursor))

        it.onSuccess(Pair(result.next_max_id, result.users))
    }

    fun getAllFollowers(): Single<List<InstagramUserSummary>> = Single.create {
        val followers = ArrayList<InstagramUserSummary>()
        var cursor = ""
        var hasNext = true

        while (hasNext) {
            val result = instagram.sendRequest(InstagramGetUserFollowersRequest(instagram.userId, cursor))
            followers.addAll(result.users)
            if (result.next_max_id.isNullOrBlank())
                hasNext = false
            else
                cursor = result.next_max_id
        }
        it.onSuccess(followers)
    }

    fun getFollowing(cursor: String): Single<Pair<String?, List<InstagramUserSummary>>> = Single.create {
        val result = instagram.sendRequest(InstagramGetUserFollowingRequest(instagram.userId, cursor))

        it.onSuccess(Pair(result.next_max_id, result.users))
    }

    fun getAllFollowing(): Single<List<InstagramUserSummary>> = Single.create {
        val followers = ArrayList<InstagramUserSummary>()
        var cursor = ""
        var hasNext = true

        while (hasNext) {
            val result = instagram.sendRequest(InstagramGetUserFollowingRequest(instagram.userId, cursor))
            followers.addAll(result.users)
            if (result.next_max_id.isNullOrBlank())
                hasNext = false
            else
                cursor = result.next_max_id
        }
        it.onSuccess(followers)
    }

    fun getUnfollowers(): Single<List<InstagramUserSummary>> = Single.zip(
            getAllFollowers(),
            getAllFollowing(),
            BiFunction { followers: List<InstagramUserSummary>, following: List<InstagramUserSummary> -> following.subtract(followers).toList() }
    )

    fun unfollow(userId: Long): Completable = Completable.create {
        instagram.sendRequest(InstagramUnfollowRequest(userId))
        it.onComplete()
    }

}