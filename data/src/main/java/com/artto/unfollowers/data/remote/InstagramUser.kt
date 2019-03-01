package com.artto.unfollowers.data.remote

import dev.niekirk.com.instagram4android.requests.payload.InstagramUserSummary

class InstagramUser() : InstagramUserSummary() {

    var isFollower = false
    var isFollowedByUser = false

    constructor(user: InstagramUserSummary, isFollower: Boolean, isFollowedByUser: Boolean) : this() {
        is_verified = user.is_verified
        profile_pic_id = user.profile_pic_id
        is_favorite = user.is_favorite
        is_private = user.is_private
        username = user.username
        pk = user.pk
        profile_pic_url = user.profile_pic_url
        has_anonymous_profile_picture = user.has_anonymous_profile_picture
        full_name = user.full_name

        this.isFollower = isFollower
        this.isFollowedByUser = isFollowedByUser
    }

}