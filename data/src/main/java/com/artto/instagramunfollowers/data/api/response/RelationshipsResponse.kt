package com.artto.instagramunfollowers.data.api.response

import com.artto.instagramunfollowers.data.entity.User
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(using = RelationshipsDeserializer::class)
class RelationshipsResponse : BaseResponse() {

    var count: Int = 0
    var hasNextPage: Boolean = false
    var endCursor: String = ""
    var items: ArrayList<User> = arrayListOf()

}