package com.nnss.dev.cftest.data.remote

import com.nnss.dev.cftest.data.remote.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*


interface Api {
    @POST("account/create")
    suspend fun createUser(
        @Body request: CreateUserRequest?
    ): Response<GenericResponse>

    @POST("account/login")
    suspend fun login(
        @Body request: LoginRequest?
    ): Response<GenericResponse>

    @GET("feed")
    suspend fun getFeed(): Response<List<FeedResponse>>

    @POST("feed/post")
    suspend fun newPost(
        @Body request: NewPostRequest?
    ): Response<FeedResponse>

    @POST("feed/{path}/comment")
    suspend fun newComment(
        @Path("path") id: String?,
        @Body request: NewCommentRequest?
    ): Response<FeedResponse>

    @GET("user/{path}")
    suspend fun getUser(
        @Path("path") user: String
    ): Response<UserInfo>

    @POST("user/picture")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequest?
    ): Response<GenericResponse>

    @POST("user/picture")
    suspend fun uploadImage(
        @Body body: MultipartBody?
    ): Response<GenericResponse>

    @PUT("account/logout")
    suspend fun logout(
    ): Response<GenericResponse>
}