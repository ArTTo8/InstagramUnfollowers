package com.artto.instagramunfollowers.data.api.response

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = UserProfileDeserializer::class)
open class UserProfileResponse : BaseResponse() {

    var id = ""
    var userName = ""
    var fullName = ""

    var avatarUrl = ""
    var avatarThumbnailUrl = ""

    var followersCount = 0
    var followingCount = 0

}