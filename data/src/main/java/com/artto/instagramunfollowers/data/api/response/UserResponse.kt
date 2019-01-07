package com.artto.instagramunfollowers.data.api.response

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = UserDeserializer::class)
class UserResponse : BaseResponse() {

    var id = ""
    var userName = ""
    var fullName = ""
    var avatarThumbnailUrl = ""
    var followersCount = 0
    var followingCount = 0

}