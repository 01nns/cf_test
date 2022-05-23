package com.nnss.dev.cftest.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GenericResponse(
    @SerializedName("code")
    @Expose
    val code: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("token")
    @Expose
    val token: String? = null
)