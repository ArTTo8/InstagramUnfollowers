package com.artto.instagramunfollowers.data.api

object ApiConstants {

    const val BASE_URL = "https://www.instagram.com/"

    object Headers {
        const val CRSF_TOKEN = "x-csrftoken"
        const val INST_AJAX = "x-instagram-ajax"
    }

    object QueryHashes {
        const val GET_FOLLOWING = "c56ee0ae1f89cdbd1c89e2bc6b8f3d18"
        const val GET_FOLLOWERS = "56066f031e6239f35a904ac20c9f37d9"
    }

    object Cookies {
        const val CRSF_TOKEN = "csrftoken"
        const val USER_ID = "ds_user_id"
        const val SESSION_ID = "sessionid"
        const val URL_GEN = "urlgen"
        const val MID = "mid"
        const val MCD = "mcd"
        const val SHBTS = "shbts"
        const val SHBID = "shbid"
    }

}