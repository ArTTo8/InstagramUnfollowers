package com.artto.instagramunfollowers.data.repository

import com.artto.instagramunfollowers.data.api.ApiConstants
import com.artto.instagramunfollowers.data.api.ApiMethods
import com.artto.instagramunfollowers.data.api.mapper.mapUserResponseToUser
import com.artto.instagramunfollowers.data.api.request.RelationshipsRequest
import com.artto.instagramunfollowers.data.api.response.AuthorizationResponse
import com.artto.instagramunfollowers.data.api.response.RelationshipsResponse
import com.artto.instagramunfollowers.data.entity.User
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Single

class InstagramRepository(private val api: ApiMethods,
                          private val objectMapper: ObjectMapper) {

    fun login(username: String, password: String): Single<AuthorizationResponse> = api.loadBaseUrl()
            .flatMap { api.authorize(username, password) }

    fun getUserProfileDetails(username: String): Single<User> = api.getUserProfileDetails(username)
            .map { mapUserResponseToUser(it) }

    fun getUserFollowing(userId: String, cursor: String, count: Int): Single<RelationshipsResponse> =
            Single.just(RelationshipsRequest(userId = userId, first = count, after = cursor))
                    .map { this.objectMapper.writeValueAsString(it) }
                    .flatMap { api.getUserRelationships(ApiConstants.QueryHashes.GET_FOLLOWING, it) }

    fun getUserFollowers(userId: String, cursor: String, count: Int): Single<RelationshipsResponse> =
            Single.just(RelationshipsRequest(userId = userId, first = count, after = cursor))
                    .map { this.objectMapper.writeValueAsString(it) }
                    .flatMap { api.getUserRelationships(ApiConstants.QueryHashes.GET_FOLLOWERS, it) }

}