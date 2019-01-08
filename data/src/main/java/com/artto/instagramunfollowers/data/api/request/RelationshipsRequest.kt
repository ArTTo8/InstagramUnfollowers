package com.artto.instagramunfollowers.data.api.request

import com.fasterxml.jackson.annotation.JsonProperty

class RelationshipsRequest(
        @field:JsonProperty("id") val userId: String,
        @field:JsonProperty("include_reel") val includeReel: Boolean = false,
        @field:JsonProperty("fetch_mutual") val fetchMutal: Boolean = true,
        @field:JsonProperty("first") val first: Int,
        @field:JsonProperty("after") val after: String
) : BaseRequest()