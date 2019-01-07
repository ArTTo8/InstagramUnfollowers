package com.artto.instagramunfollowers.data.interactor

import com.artto.instagramunfollowers.data.entity.User
import com.artto.instagramunfollowers.data.exception.InstagramAuthorizationException
import com.artto.instagramunfollowers.data.repository.ApplicationUserRepository
import com.artto.instagramunfollowers.data.repository.CookieRepository
import com.artto.instagramunfollowers.data.repository.InstagramRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class InstagramInteractor(private val instagramRepository: InstagramRepository,
                          private val userRepository: ApplicationUserRepository,
                          private val cookieRepository: CookieRepository) {

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
            .subscribeOn(Schedulers.io())

}