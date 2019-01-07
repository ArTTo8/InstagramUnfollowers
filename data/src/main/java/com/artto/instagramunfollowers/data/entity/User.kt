package com.artto.instagramunfollowers.data.entity

data class User(
        var id: String = "",
        var userName: String = "",
        var fullName: String = "",
        var avatarThumbnailUrl : String= "",
        var followersCount: Int = 0,
        var followingCount:Int = 0,

        var isAuthorized: Boolean = false,
        var password: String = ""
)