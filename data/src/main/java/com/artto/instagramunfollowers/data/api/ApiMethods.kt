package com.artto.instagramunfollowers.data.api

import com.artto.instagramunfollowers.data.api.response.AuthorizationResponse
import com.artto.instagramunfollowers.data.api.response.UserResponse
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiMethods {

    @GET(".")
    fun loadBaseUrl(): Single<ResponseBody>

    @FormUrlEncoded
    @POST("accounts/login/ajax/")
    fun authorize(@Field("username") username: String, @Field("password") password: String): Single<AuthorizationResponse>

    @GET("{username}/?__a=1")
    fun getUserProfileDetails(@Path("username") userName: String): Single<UserResponse>

}