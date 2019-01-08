package com.artto.instagramunfollowers.data.api.response

import com.artto.instagramunfollowers.data.api.mapper.mapUserResponseToUser
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

class RelationshipsDeserializer(vc: Class<*>? = null) : StdDeserializer<RelationshipsResponse>(vc) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): RelationshipsResponse =
            RelationshipsResponse().apply {
                val rootNode = p.codec.readTree<JsonNode>(p)
                        .get("data")
                        .get("user")
                        .get("edge_followed_by")

                count = rootNode.get("count").intValue()

                with(rootNode.get("page_info")) {
                    hasNextPage = get("has_next_page").booleanValue()
                    endCursor = get("end_cursor").textValue()
                }

                rootNode.get("edges")
                        .forEach { items.add(mapUserResponseToUser(parseUser(it.get("node")))) }

            }

    private fun parseUser(userNode: JsonNode): UserResponse = UserResponse().apply {
        with(userNode) {
            id = get("id").textValue()
            userName = get("username").textValue()
            fullName = get("full_name").textValue()
            avatarThumbnailUrl = get("profile_pic_url").textValue()
        }
    }
}