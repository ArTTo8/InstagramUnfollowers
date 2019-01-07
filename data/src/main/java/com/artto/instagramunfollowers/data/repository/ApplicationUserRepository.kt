package com.artto.instagramunfollowers.data.repository

import com.artto.instagramunfollowers.data.ApplicationUserDataStore
import com.artto.instagramunfollowers.data.entity.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

class ApplicationUserRepository(private val dataStore: ApplicationUserDataStore) {

    private val userBehaviourSubject: BehaviorSubject<User> =
            BehaviorSubject.createDefault(dataStore.load())

    val userObservable: Observable<User> = userBehaviourSubject.map { it.copy() }

    val userSingle: Single<User> = userBehaviourSubject.firstOrError().map { it.copy() }

    fun update(instagramUserEntity: User) {
        dataStore.save(instagramUserEntity)
        userBehaviourSubject.onNext(instagramUserEntity)
    }

    fun clear() = update(User())
}