package com.artto.instagramunfollowers.data.interactor

import com.artto.instagramunfollowers.data.api.response.RelationshipsResponse
import com.artto.instagramunfollowers.data.entity.User
import com.artto.instagramunfollowers.data.exception.InstagramAuthorizationException
import com.artto.instagramunfollowers.data.repository.ApplicationUserRepository
import com.artto.instagramunfollowers.data.repository.CookieRepository
import com.artto.instagramunfollowers.data.repository.InstagramRepository
import io.reactivex.Completable
import io.reactivex.Single

class InstagramInteractor(private val instagramRepository: InstagramRepository,
                          private val userRepository: ApplicationUserRepository,
                          private val cookieRepository: CookieRepository) {

    fun getUserObservable() = userRepository.userObservable

    fun getUserSingle() = userRepository.userSingle

    fun login(username: String, password: String): Single<User> = logout()
            .andThen(instagramRepository.login(username, password))
            .doOnSuccess { if (!it.isAuthenticated) throw InstagramAuthorizationException() }
            .flatMap { instagramRepository.getUserProfileDetails(username) }
            .doOnSuccess {
                it.isAuthorized = true
                it.password = password
                userRepository.update(it)
            }

    fun logout(): Completable = Completable
            .fromAction {
                userRepository.clear()
                cookieRepository.clear()
            }

    fun getUserProfileDetails(username: String): Single<User> =
            instagramRepository.getUserProfileDetails(username)

    fun getUserFollowing(userId: String, cursor: String, count: Int): Single<RelationshipsResponse> =
            instagramRepository.getUserFollowing(userId, cursor, count)

    fun getUserFollowers(userId: String, cursor: String, count: Int): Single<RelationshipsResponse> =
            instagramRepository.getUserFollowers(userId, cursor, count)

}