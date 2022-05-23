package com.nnss.dev.cftest.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateUserRequest(
    @SerializedName("username")
    @Expose
    val username: String? = null,

    @SerializedName("password")
    @Expose
    val password: String? = null,

    @SerializedName("firstname")
    @Expose
    val firstname: String? = null,

    @SerializedName("lastname")
    @Expose
    val lastname: String? = null
)

data class LoginRequest(
    @SerializedName("username")
    @Expose
    val username: String? = null,

    @SerializedName("password")
    @Expose
    val password: String? = null
)

data class UpdateProfileRequest(
    @SerializedName("profileImage")
    @Expose
    val profileImage: String? = null
)

data class UserInfo(
    @SerializedName("username")
    @Expose
    val username: String? = null,

    @SerializedName("profilePic")
    @Expose
    val profilePic: String? = null,

    @SerializedName("firstName")
    @Expose
    val firstName: String? = null,

    @SerializedName("lastName")
    @Expose
    val lastName: String? = null
)