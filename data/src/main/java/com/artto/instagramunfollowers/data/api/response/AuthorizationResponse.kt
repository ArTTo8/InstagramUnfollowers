package com.artto.instagramunfollowers.data.api.response

import com.fasterxml.jackson.annotation.JsonProperty

class AuthorizationResponse : BaseResponse() {

    @JsonProperty("authenticated")
    var isAuthenticated: Boolean = false

    @JsonProperty("userId")
    var userId = ""
}