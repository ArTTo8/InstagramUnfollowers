package com.artto.instagramunfollowers.data.api.response

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.io.IOException

class UserDeserializer(vc: Class<*>? = null) : StdDeserializer<UserResponse>(vc) {

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext) = UserResponse().apply {

        val userNode = jp.codec.readTree<JsonNode>(jp)
                .get("graphql")
                .get("user")

        with(userNode) {
            id = get("id").textValue()
            userName = get("username").textValue()
            fullName = get("full_name").textValue()
            avatarThumbnailUrl = get("profile_pic_url").textValue()
            followersCount = get("edge_followed_by").get("count").intValue()
            followingCount = get("edge_follow").get("count").intValue()
        }
    }

}