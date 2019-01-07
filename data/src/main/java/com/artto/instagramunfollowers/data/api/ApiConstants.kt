package com.artto.instagramunfollowers.data.api

object ApiConstants {

    const val BASE_URL = "https://www.instagram.com/"

    object Headers {
        const val CRSF_TOKEN = "x-csrftoken"
        const val INST_AJAX = "x-instagram-ajax"
    }

    object QueryHashes {
        const val GET_USER = "7c16654f22c819fb63d1183034a5162f"
        const val GET_USER_POSTS = "a5164aed103f24b03e7b7747a2d94e3c"
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