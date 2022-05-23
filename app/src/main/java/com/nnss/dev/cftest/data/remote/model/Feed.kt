package com.nnss.dev.cftest.data.remote.model

import android.os.Parcelable
import com.google.gson.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.lang.reflect.Type
import kotlin.collections.ArrayList


@Parcelize
data class FeedResponse(
    @SerializedName("createdAt")
    @Expose
    val createdAt: String? = null,

    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("text")
    @Expose
    val text: String? = null,

    @SerializedName("updatedAt")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("username")
    @Expose
    val username: String? = null,

    @SerializedName("user")
    @Expose
    val user: User? = null,

    @SerializedName("comments")
    @JsonAdapter(ValueClassTypeAdapter::class)
    @Expose
    val comments: @RawValue List<Comments>? = null

) : Parcelable

@Parcelize
data class Comments(
    @SerializedName("createdAt")
    @Expose
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("id")
    @Expose
    val id: String? = null,

    @SerializedName("text")
    @Expose
    val text: String? = null,

    @SerializedName("username")
    @Expose
    val username: String? = null,

    @SerializedName("timestamp")
    @Expose
    val timestamp: String? = null
) : Parcelable

@Parcelize
data class User(
    @SerializedName("username")
    @Expose
    val username: String? = null,

    @SerializedName("firstName")
    @Expose
    val firstName: String? = null,

    @SerializedName("lastName")
    @Expose
    val lastName: String? = null,

    @SerializedName("profilePic")
    @Expose
    val profilePic: String? = null
) : Parcelable

data class NewPostRequest(
    @SerializedName("text")
    @Expose
    val text: String? = null
)

data class NewCommentRequest(
    @SerializedName("text")
    @Expose
    val text: String? = null
)

class ValueClassTypeAdapter :
    JsonDeserializer<ArrayList<Comments?>?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        ctx: JsonDeserializationContext
    ): ArrayList<Comments?> {
        return getJSONArray(json, Comments::class.java, ctx)
    }

    private fun <T> getJSONArray(json: JsonElement, type: Type, ctx:
    JsonDeserializationContext): ArrayList<T> {
        val list = ArrayList<T>()
        when {
            json.isJsonArray -> {
                for (e in json.asJsonArray) {
                    list.add(ctx.deserialize<Any>(e, type) as T)
                }
            }
            json.isJsonObject -> {
                list.add(ctx.deserialize<Any>(json, type) as T)
            }
            else -> {
                throw RuntimeException("Unexpected JSON type: " + json.javaClass)
            }
        }
        return list
    }
}