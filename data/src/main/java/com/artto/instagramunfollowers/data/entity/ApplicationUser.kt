package com.artto.instagramunfollowers.data.entity

import com.artto.instagramunfollowers.data.api.response.UserProfileResponse

class ApplicationUser : UserProfileResponse() {

    var isAuthorized = false
    var userPassword = ""

}