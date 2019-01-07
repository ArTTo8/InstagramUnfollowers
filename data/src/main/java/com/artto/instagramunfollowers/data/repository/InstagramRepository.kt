package com.artto.instagramunfollowers.data.repository

import com.artto.instagramunfollowers.data.api.ApiMethods
import com.artto.instagramunfollowers.data.api.response.AuthorizationResponse
import com.artto.instagramunfollowers.data.entity.User
import io.reactivex.Single

class InstagramRepository(private val apiMethods: ApiMethods) {

    fun login(username: String, password: String): Single<AuthorizationResponse> = apiMethods.loadBaseUrl()
            .flatMap { apiMethods.authorize(username, password) }

    fun getUserProfileDetails(username: String): Single<User> = apiMethods.getUserProfileDetails(username)
            .map { User(it.id, it.userName, it.fullName, it.avatarThumbnailUrl, it.followersCount, it.followingCount) }


}