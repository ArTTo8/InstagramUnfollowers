package com.artto.instagramunfollowers.data.api.mapper

import com.artto.instagramunfollowers.data.api.response.UserResponse
import com.artto.instagramunfollowers.data.entity.User

fun mapUserResponseToUser(userResponse: UserResponse): User = with(userResponse) {
    User(id, userName, fullName, avatarThumbnailUrl, followersCount, followingCount)
}