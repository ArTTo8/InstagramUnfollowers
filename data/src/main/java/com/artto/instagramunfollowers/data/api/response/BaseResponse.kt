package com.artto.instagramunfollowers.data.api.response

import com.fasterxml.jackson.annotation.JsonProperty

open class BaseResponse {

    @JsonProperty("status")
    var status = "ok"

}